resource "google_project_service" "repo" {
  service            = "sourcerepo.googleapis.com"
  disable_on_destroy = false
}

resource "google_sourcerepo_repository" "cloud-run-repo" {
  name       = var.cloud_run_repo_name
  depends_on = [google_project_service.repo]
}