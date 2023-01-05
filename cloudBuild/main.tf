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

  trigger_template {
    repo_name   = var.cloud_run_repo_name
    branch_name = var.branch_name
  }

  build {
    step {
      name = "gcr.io/cloud-builders/docker"
      args = ["build", "-t", "gcr.io/$PROJECT_ID/todolist-app", "."]
    }

    step {
      name = "gcr.io/cloud-builders/docker"
      args = ["push", "gcr.io/$PROJECT_ID/todolist-app"]
    }

    step {
      name = "gcr.io/cloud-builders/gcloud"
      args = ["run", "deploy", "app", "--image", "gcr.io/$PROJECT_ID/todolist-app", "--region", "europe-west3", "--platform", "managed", "--allow-unauthenticated"]
    }

  }

  filename   = var.cloud_build_filename
  depends_on = [google_project_service.build]
}