resource "google_project_service" "build" {
  service            = "cloudbuild.googleapis.com"
  disable_on_destroy = false
}

resource "google_project_service" "registry" {
  service            = "containerregistry.googleapis.com"
  disable_on_destroy = false
}

resource "google_cloudbuild_trigger" "cloud_build_trigger" {
  depends_on = [google_project_service.build]
  name = var.cloud_run_repo_name

  trigger_template {
    repo_name = var.cloud_run_repo_name
    branch_name = var.branch_name
  }

  build {
    step {
      name = "gcr.io/cloud-builders/gcloud"
      entrypoint = "/bin/bash"
      args = ["-c", "cd ~"]
    }

    step {
      name = "gcr.io/cloud-builders/gcloud"
      entrypoint = "/bin/bash"
      args = ["-c", "pwd"]
    }

    step {
      name = "gcr.io/cloud-builders/gcloud"
      entrypoint = "/bin/bash"
      args = ["-c", "ls"]
    }

    step {
      name = "gcr.io/cloud-builders/docker"
      args = ["build", "-t", "gcr.io/${var.project_id}/todolist-app", "."]
    }

    step {
      name = "gcr.io/cloud-builders/gcloud"
      entrypoint = "/bin/bash"
      args = ["-c", "echo AFTER_FIRST_STEP"]
    }

    step {
      name   = "gcr.io/cloud-builders/docker"
      args = ["push", "gcr.io/${var.project_id}/todolist-app"]
    }

    step {
      name = "gcr.io/cloud-builders/gcloud"
      entrypoint = "/bin/bash"
      args = ["-c", "echo AFTER_SECOND_STEP"]
    }


    step {
      name = "gcr.io/cloud-builders/gcloud"
      args = ["run", "deploy", "gcp-proj-app", "--image", "gcr.io/${var.project_id}/todolist-app", "--region", "europe-west3", "--platform", "managed", "--allow-unauthenticated"]
    }

    step {
      name = "gcr.io/cloud-builders/gcloud"
      entrypoint = "/bin/bash"
      args = ["-c", "echo AFTER_THIRD_STEP"]
    }
  }
}

resource "null_resource" "empty_commit" {
  depends_on = [google_cloudbuild_trigger.cloud_build_trigger]
  provisioner "local-exec" {
    command = "cd && cd gcp-proj && git commit --allow-empty -m 'Trigger build' && git push google"
  }
}

resource "google_project_service" "run" {
  service            = "run.googleapis.com"
  disable_on_destroy = false
  depends_on = [null_resource.empty_commit]
}

data "google_container_registry_image" "project-app" {
  name = var.image_name
}

resource "google_cloud_run_service" "server" {
  depends_on = [google_project_service.run]
  name     = var.cloud_run_name
  location = var.region

  template {
    spec {
      containers {
        image = data.google_container_registry_image.project-app.image_url
      }
    }
  }

  traffic {
    percent         = 100
    latest_revision = true
  }
}

data "google_iam_policy" "all_users_policy" {
  binding {
    role    = "roles/run.invoker"
    members = ["allUsers"]
  }
}

resource "google_cloud_run_service_iam_policy" "all_users_iam_policy" {
  location    = google_cloud_run_service.server.location
  service     = google_cloud_run_service.server.name
  policy_data = data.google_iam_policy.all_users_policy.policy_data
}

resource "google_sql_database_instance" "db_instance" {
  name             = var.cloud_sql_db_instance_name
  region           = var.region
  database_version = "POSTGRES_14"

  settings {
    tier = "db-f1-micro"

    location_preference {
      zone = var.zone
    }
  }

  deletion_protection = "false"
}