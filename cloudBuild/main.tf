resource "google_project_service" "build" {
  service            = "cloudbuild.googleapis.com"
  disable_on_destroy = false
}

resource "google_project_service" "registry" {
  service            = "containerregistry.googleapis.com"
  disable_on_destroy = false
}

resource "google_cloudbuild_trigger" "cloud_build_trigger" {
  name = var.cloud_run_repo_name

  github {
    owner = var.cloud_run_repo_owner
    name  = var.cloud_run_repo_name
    push {
      branch = var.branch_name
    }
  }

  build {
    step {
      name = "gcr.io/cloud-builders/docker"
      args = ["build", "-t", "gcr.io/${var.project_id}/todolist-app", "."]
    }

    step {
      name   = "gcr.io/cloud-builders/docker"
      args = ["push", "gcr.io/${var.project_id}/todolist-app"]
    }

    step {
      name = "gcr.io/cloud-builders/gcloud"
      args = ["run", "deploy", "app", "--image", "gcr.io/${var.project_id}/todolist-app", "--region", "europe-west3", "--platform", "managed", "--allow-unauthenticated"]
    }
  }

  depends_on = [google_project_service.build]
}

resource "null_resource" "empty_commit" {
  depends_on = [google_cloudbuild_trigger.cloud_build_trigger]
  provisioner "local-exec" {
    command = "cd && cd gcp-proj && git commit --allow-empty -m 'Trigger build' && git push"
  }
}