terraform {
  required_providers {
    google = {
      source = "hashicorp/google"
    }
  }
}

provider "google" {
  version = "4.47.0"
  project = var.project_id
  region  = var.region
  zone    = var.zone
}