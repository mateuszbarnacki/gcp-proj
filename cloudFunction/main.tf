resource "google_project_service" "pubsub" {
  service            = "pubsub.googleapis.com"
  disable_on_destroy = false
}

resource "google_pubsub_topic" "pubsub-topic" {
  name = var.pubsub_topic_name
  message_storage_policy {
    allowed_persistence_regions = [
      var.region,
    ]
  }
  message_retention_duration = "86400s"
}

resource "google_project_service" "storage-json" {
  service            = "storage-api.googleapis.com"
  disable_on_destroy = false
}

resource "google_project_service" "storage" {
  service            = "storage.googleapis.com"
  disable_on_destroy = false
}

resource "google_storage_bucket" "bucket" {
  name          = var.bucket_name
  location      = "EU"
  force_destroy = true
}

resource "google_storage_bucket_object" "bucket_object" {
  name   = var.bucket_object_name
  bucket = google_storage_bucket.bucket.name
  source = var.cloud_function_source
}

resource "google_project_service" "function" {
  service            = "cloudfunctions.googleapis.com"
  disable_on_destroy = false
}

resource "google_project_service" "eventarc" {
  service            = "eventarc.googleapis.com"
  disable_on_destroy = false
}

resource "google_project_service" "artifact_registry" {
  service            = "artifactregistry.googleapis.com"
  disable_on_destroy = false
}

resource "google_cloudfunctions2_function" "function" {
  name        = var.cloud_function_name
  location    = var.region
  description = "This function sends email when user create new todo item"

  build_config {
    runtime     = "nodejs16"
    entry_point = var.cloud_function_entry_point
    source {
      storage_source {
        bucket = google_storage_bucket.bucket.name
        object = google_storage_bucket_object.bucket_object.name
      }
    }
  }

  service_config {
    min_instance_count    = 1
    max_instance_count    = 5
    available_memory      = "256M"
    timeout_seconds       = 60

    environment_variables = {
      APP_PROJECT_ID = "${var.project_id}"
      APP_MAIL_USERNAME_TEST = "${var.mail_username_test}"
    }

    secret_environment_variables {
      key        = "MAIL_USERNAME"
      project_id = var.project_id
      secret     = google_secret_manager_secret.mail-username-secret.secret_id
      version    = "latest"
    }

    secret_environment_variables {
      key        = "MAIL_PASSWORD"
      project_id = var.project_id
      secret     = google_secret_manager_secret.mail-password-secret.secret_id
      version    = "latest"
    }

    secret_environment_variables {
      key        = "OAUTH_CLIENTID"
      project_id = var.project_id
      secret     = google_secret_manager_secret.oauth-client_id-secret.secret_id
      version    = "latest"
    }

    secret_environment_variables {
      key        = "OAUTH_CLIENT_SECRET"
      project_id = var.project_id
      secret     = google_secret_manager_secret.oauth-client_secret-secret.secret_id
      version    = "latest"
    }

    secret_environment_variables {
      key        = "OAUTH_REFRESH_TOKEN"
      project_id = var.project_id
      secret     = google_secret_manager_secret.oauth-refresh-token-secret.secret_id
      version    = "latest"
    }

    secret_environment_variables {
      key        = "OAUTH_ACCESS_TOKEN"
      project_id = var.project_id
      secret     = google_secret_manager_secret.oauth-access-token-secret.secret_id
      version    = "latest"
    }

    all_traffic_on_latest_revision = true
    service_account_email = google_service_account.cloud_function_service_account.email
  }

  event_trigger {
    trigger_region = var.region
    event_type     = "google.cloud.pubsub.topic.v1.messagePublished"
    pubsub_topic   = google_pubsub_topic.pubsub-topic.id
    retry_policy   = "RETRY_POLICY_RETRY"
  }

  depends_on = [
    google_project_service.function,
    google_project_service.pubsub,
    google_project_service.storage,
    google_project_service.storage-json,
    google_project_service.secret_manager,
    google_project_service.artifact_registry,
    google_project_service.eventarc,
    google_service_account.cloud_function_service_account,
    google_secret_manager_secret_version.mail-username-secret-ver,
    google_secret_manager_secret_version.mail-password-secret-ver,
    google_secret_manager_secret_version.oauth-client_id-secret-ver,
    google_secret_manager_secret_version.oauth-client_secret-secret-ver,
    google_secret_manager_secret_version.oauth-refresh-token-secret-ver,
    google_secret_manager_secret_version.oauth-access-token-secret-ver
  ]
}

data "archive_file" "init" {
  type        = "zip"
  source_dir  = "${path.module}/../cloudFunction"
  excludes    = ["${path.module}/main.tf",
    "${path.module}/output.tf",
    "${path.module}/variables.tf"]
  output_path = var.cloud_function_source
}

resource "google_service_account" "cloud_function_service_account" {
  account_id   = var.account_id
  display_name = var.display_name
}

resource "google_project_iam_binding" "secret_accessor_binding" {
  project    = var.project_id
  role       = "roles/secretmanager.secretAccessor"
  members    = ["serviceAccount:${var.account_id}@${var.project_id}.iam.gserviceaccount.com",]
  depends_on = [google_service_account.cloud_function_service_account]
}

resource "google_project_iam_binding" "run_admin_binding" {
  project    = var.project_id
  role       = "roles/run.admin"
  members    = ["serviceAccount:${var.account_id}@${var.project_id}.iam.gserviceaccount.com",]
  depends_on = [google_service_account.cloud_function_service_account]
}

resource "google_project_service" "secret_manager" {
  service            = "secretmanager.googleapis.com"
  disable_on_destroy = false
}

resource "google_secret_manager_secret" "mail-username-secret" {
  secret_id = "mail-username"

  replication {
    user_managed {
      replicas {
        location = var.region
      }
    }
  }
}

resource "google_secret_manager_secret" "mail-password-secret" {
  secret_id = "mail-password"

  replication {
    user_managed {
      replicas {
        location = var.region
      }
    }
  }
}

resource "google_secret_manager_secret" "oauth-client_id-secret" {
  secret_id = "oauth-client-id"

  replication {
    user_managed {
      replicas {
        location = var.region
      }
    }
  }
}

resource "google_secret_manager_secret" "oauth-client_secret-secret" {
  secret_id = "oauth-client-secret"

  replication {
    user_managed {
      replicas {
        location = var.region
      }
    }
  }
}

resource "google_secret_manager_secret" "oauth-refresh-token-secret" {
  secret_id = "oauth-refresh-token"

  replication {
    user_managed {
      replicas {
        location = var.region
      }
    }
  }
}

resource "google_secret_manager_secret" "oauth-access-token-secret" {
  secret_id = "oauth-access-token"

  replication {
    user_managed {
      replicas {
        location = var.region
      }
    }
  }
}

resource "google_secret_manager_secret_version" "mail-username-secret-ver" {
  secret      = google_secret_manager_secret.mail-username-secret.name
  secret_data = var.mail_username
  enabled     = true
}

resource "google_secret_manager_secret_version" "mail-password-secret-ver" {
  secret      = google_secret_manager_secret.mail-password-secret.name
  secret_data = var.mail_password
  enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-client_id-secret-ver" {
  secret      = google_secret_manager_secret.oauth-client_id-secret.name
  secret_data = var.oauth_clientId
  enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-client_secret-secret-ver" {
  secret      = google_secret_manager_secret.oauth-client_secret-secret.name
  secret_data = var.oauth_client_secret
  enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-refresh-token-secret-ver" {
  secret      = google_secret_manager_secret.oauth-refresh-token-secret.name
  secret_data = var.oauth_refresh_token
  enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-access-token-secret-ver" {
  secret      = google_secret_manager_secret.oauth-access-token-secret.name
  secret_data = var.oauth_access_token
  enabled     = true
}