terraform {
    required_providers {
        google = {
            source = "hashicorp/google"
        }
    }
}

locals {
    project = var.PROJECT_ID
}

provider "google" {
    version = "4.47.0"
    project = local.project
    region  = "europe-west3"
    zone    = "europe-west3-b"
}

resource "google_pubsub_topic" "pubsub-topic" {
    name = "confirmation-topic"
    message_storage_policy {
        allowed_persistence_regions = [
            "europe-west3",
        ]
    }
    message_retention_duration = "86400s"
}

resource "google_cloudfunctions2_function" "function" {
    name        = "confirmation-handler"
    location    = "europe-west3"
    description = "This function sends email when user create new todo item"

    build_config {
        runtime    = "nodejs16"
        entryPoint = "confirmationHandler"
        source {
            repo_source {
                branch_name = "main"
                dir         = "cloud-functions/confirmationHandler"
                project_id  = local.project
                repo_name   = "mateuszbarnacki/gcp-proj"
            }
        }
    }

    service_config {
        min_instance_count = 1
        max_instance_count = 5
        available_memory   = "256M"

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
            key        = "MAIL_USERNAME_TEST"
            project_id = local.project
            secret     = google_secret_manager_secret.mail-username-test-secret.secret_id
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
        trigger_region = "europe-west3"
        event_type     = "google.cloud.pubsub.topic.v2.messagePublished"
        pubsub_topic   = google_pubsub_topic.pubsub-topic.id
        retry_policy   = "RETRY_POLICY_RETRY"
    }

    depends_on = [
        google_secret_manager_secret_version.mail-username-secret-ver,
        google_secret_manager_secret_version.mail-password-secret-ver,
        google_secret_manager_secret_version.mail-username-test-secret-ver,
        google_secret_manager_secret_version.oauth-client_id-secret-ver,
        google_secret_manager_secret_version.oauth-client_secret-secret-ver,
        google_secret_manager_secret_version.oauth-refresh-token-secret-ver,
        google_secret_manager_secret_version.oauth-access-token-secret-ver
    ]
}

resource "google_secret_manager_secret" "mail-username-secret" {
    secret_id = "mail-username"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "mail-password-secret" {
    secret_id = "mail-password"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "mail-username-test-secret" {
    secret_id = "mail-username-test"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "oauth-client_id-secret" {
    secret_id = "oauth-client-id"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "oauth-client_secret-secret" {
    secret_id = "oauth-client-secret"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "oauth-refresh-token-secret" {
    secret_id = "oauth-refresh-token"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret" "oauth-access-token-secret" {
    secret_id = "oauth-access-token"

    replication {
        user_managed {
            replicas {
                location = "europe-west3"
            }
        }
    }
}

resource "google_secret_manager_secret_version" "mail-username-secret-ver" {
    secret      = google_secret_manager_secret.mail-username-secret.name
    secret_data = var.MAIL_USERNAME
    enabled     = true
}

resource "google_secret_manager_secret_version" "mail-password-secret-ver" {
    secret      = google_secret_manager_secret.mail-password-secret.name
    secret_data = var.MAIL_PASSWORD
    enabled     = true
}

resource "google_secret_manager_secret_version" "mail-username-test-secret-ver" {
    secret      = google_secret_manager_secret.mail-username-test-secret.name
    secret_data = var.MAIL_USERNAME_TEST
    enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-client_id-secret-ver" {
    secret      = google_secret_manager_secret.oauth-client_id-secret.name
    secret_data = var.OAUTH_CLIENTID
    enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-client_secret-secret-ver" {
    secret      = google_secret_manager_secret.oauth-client_secret-secret.name
    secret_data = var.OAUTH_CLIENT_SECRET
    enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-refresh-token-secret-ver" {
    secret      = google_secret_manager_secret.oauth-refresh-token-secret.name
    secret_data = var.OAUTH_REFRESH_TOKEN
    enabled     = true
}

resource "google_secret_manager_secret_version" "oauth-access-token-secret-ver" {
    secret      = google_secret_manager_secret.oauth-access-token-secret.name
    secret_data = var.OAUTH_ACCESS_TOKEN
    enabled     = true
}

output "function_uri" {
    value = google_cloudfunctions2_function.function.service_config[0].uri
}