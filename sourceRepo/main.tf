resource "google_project_service" "repo" {
  service            = "sourcerepo.googleapis.com"
  disable_on_destroy = false
}

resource "google_sourcerepo_repository" "cloud-run-repo" {
  name       = var.cloud_run_repo_name
  depends_on = [google_project_service.repo]
  provisioner "local-exec" {
    command = "pwd"
  }
  provisioner "local-exec" {
    command = "cd /sourceRepo && rm -rf ${var.cloud_run_repo_name} && gcloud source repos clone ${var.cloud_run_repo_name} && cd ${var.cloud_run_repo_name} && cp -R ~/gcp-proj/cloudRun/* . && git add * && git commit -m 'Initial commit' && git push -u origin master"
  }
}