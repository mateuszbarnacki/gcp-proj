resource "google_project_service" "repo" {
  service            = "sourcerepo.googleapis.com"
  disable_on_destroy = false
}

resource "google_sourcerepo_repository" "cloud-run-repo" {
  name       = var.cloud_run_repo_name
  depends_on = [google_project_service.repo]
}

resource "null_resource" "add_remote" {
  depends_on = [google_sourcerepo_repository.cloud-run-repo]
  provisioner "local-exec" {
    command = "cd && cd gcp-proj && git remote add google https://source.developers.google.com/p/${var.project_id}/r/${var.cloud_run_repo_name}"
  }
}