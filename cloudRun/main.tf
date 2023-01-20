resource "google_project_service" "run" {
  service            = "run.googleapis.com"
  disable_on_destroy = false
}

data "google_container_registry_image" "project-app" {
  name = var.image_name
}

resource "google_cloud_run_service" "server" {
  depends_on = [google_project_service.run]
  name       = var.cloud_run_name
  location   = var.region

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
    role    = "roles/run.admin"
    members = ["allUsers"]
  }
}

resource "google_cloud_run_service_iam_policy" "all_users_iam_policy" {
  location    = google_cloud_run_service.server.location
  service     = google_cloud_run_service.server.name
  policy_data = data.google_iam_policy.all_users_policy.policy_data
}

/*data "google_iam_policy" "service_account_policy" {
  binding {
    members = ["allUsers"]
    role    = "roles/iam.serviceAccountUser"
  }
}

resource "google_cloud_run_service_iam_policy" "service_account_iam_policy" {
  policy_data = data.google_iam_policy.service_account_policy.policy_data
  service     = google_cloud_run_service.server.name
  location    = google_cloud_run_service.server.location
}*/