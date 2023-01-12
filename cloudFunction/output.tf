output "function_uri" {
  value = google_cloudfunctions_function.function.source_repository.0.deployed_url
}