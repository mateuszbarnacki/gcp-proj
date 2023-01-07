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
    branch_name = var.branch_name
    repo_name = var.cloud_run_repo_name
  }

  filename = var.cloud_build_filename
  depends_on = [google_project_service.build]
}

resource "null_resource" "empty_commit" {
  depends_on = [google_cloudbuild_trigger.cloud_build_trigger]
  provisioner "local-exec" {
    command = "cd && cd gcp-proj/sourceRepo && git commit --allow-empty -m 'Trigger build' && git push origin master"
  }
}