terraform {
    required_providers {
        google = {
            source = "hashicorp/google"
        }
    }
}

locals {
    project = var.project_id
}

provider "google" {
    version = "4.47.0"
    project = local.project
    region  = var.region
    zone    = var.zone
}

// Cloud source repo

resource "google_project_service" "repo" {
    service            = "sourcerepo.googleapis.com"
    disable_on_destroy = false
}

resource "google_sourcerepo_repository" "cloud-run-repo" {
    name       = var.cloud_run_repo_name
    depends_on = [google_project_service.repo]
}

// PubSub

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

// Cloud Function Gen 2

resource "google_project_service" "function" {
    service            = "cloudfunctions.googleapis.com"
    disable_on_destroy = false
}

resource "google_storage_bucket" "bucket" {
    name     = var.bucket_name
    location = "EU"
}

resource "google_storage_bucket_object" "bucket_object" {
    name   = var.bucket_object_name
    bucket = google_storage_bucket.bucket.name
    source = var.cloud_function_source
}

resource "google_cloudfunctions2_function" "function" {
    name        = var.cloud_function_name
    location    = var.region
    description = "This function sends email when user create new todo item"

    build_config {
        runtime    = "nodejs16"
        entryPoint = var.cloud_function_entry_point
        source {
            storage_source {
                bucket = google_storage_bucket.bucket.name
                object = google_storage_bucket_object.bucket_object.name
            }
        }
    }

    service_config {
        min_instance_count = 1
        max_instance_count = 5
        available_memory   = "256M"

        environment_variables {
            MAIL_USERNAME_TEST = var.mail_username_test
        }

        secret_environment_variables {
            key        = "MAIL_USERNAME"
            project_id = local.project
            secret     = google_secret_manager_secret.mail-username-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "MAIL_PASSWORD"
            project_id = local.project
            secret     = google_secret_manager_secret.mail-password-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "OAUTH_CLIENTID"
            project_id = local.project
            secret     = google_secret_manager_secret.oauth-client_id-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "OAUTH_CLIENT_SECRET"
            project_id = local.project
            secret     = google_secret_manager_secret.oauth-client_secret-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "OAUTH_REFRESH_TOKEN"
            project_id = local.project
            secret     = google_secret_manager_secret.oauth-refresh-token-secret.secret_id
            version    = "latest"
        }

        secret_environment_variables {
            key        = "OAUTH_ACCESS_TOKEN"
            project_id = local.project
            secret     = google_secret_manager_secret.oauth-access-token-secret.secret_id
            version    = "latest"
        }
    }

    event_trigger {
        trigger_region = var.region
        event_type     = "google.cloud.pubsub.topic.v2.messagePublished"
        pubsub_topic   = google_pubsub_topic.pubsub-topic.id
        retry_policy   = "RETRY_POLICY_RETRY"
    }

    depends_on = [
        google_secret_manager_secret_version.mail-username-secret-ver,
        google_secret_manager_secret_version.mail-password-secret-ver,
        google_secret_manager_secret_version.oauth-client_id-secret-ver,
        google_secret_manager_secret_version.oauth-client_secret-secret-ver,
        google_secret_manager_secret_version.oauth-refresh-token-secret-ver,
        google_secret_manager_secret_version.oauth-access-token-secret-ver
    ]
}

// Secret Manager

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

// Cloud Run

resource "google_project_service" "run" {
    service            = "run.googleapis.com"
    disable_on_destroy = false
}

data "google_container_registry_image" "project-app" {
    name = var.image_name
}

resource "google_cloud_run_service" "server" {
    name     = var.cloud_run_name
    location = var.region

    template {
        spec {
            containers {
                image = data.google_container_registry_image.project-app.image_url
            }
        }
    }

    metadata {
        annotations = {
            "run.googleapis.com/cloudsql-instances" = google_sql_database_instance.db_instance.connection_name
        }
    }

    traffic {
        percent         = 100
        latest_revision = true
    }
}

data "google_iam_policy" "all_users_policy" {
    binding {
        role    = "roles/run.invoker"
        members = ["allUsers"]
    }
}

resource "google_cloud_run_service_iam_policy" "all_users_iam_policy" {
    location    = google_cloud_run_service.server.location
    service     = google_cloud_run_service.server.name
    policy_data = data.google_iam_policy.all_users_policy.policy_data
}

// Cloud SQL

resource "google_sql_database_instance" "db_instance" {
    name             = var.cloud_sql_db_instance_name
    region           = var.region
    database_version = "POSTGRES_14"

    settings {
        tier = "db-f1-micro"

        location_preference {
            zone = var.zone
        }
    }

    deletion_protection = "false"
}

// Cloud Build

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
    filename   = var.cloud_build_filename
    depends_on = [google_project_service.build]
}