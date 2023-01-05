terraform {
  required_providers {
    archive = {
      source = "hashicorp/archive"
    }
    google = {
      source = "hashicorp/google"
    }
  }
}

provider "archive" {
  version = "2.2.0"
}

provider "google" {
  version = "4.47.0"
  project = var.project_id
  region  = var.region
  zone    = var.zone
}