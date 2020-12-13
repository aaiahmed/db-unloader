resource "aws_s3_bucket" "bucket" {
  bucket = "dbunloader"
  acl    = "private"

  lifecycle_rule {
    id      = "s3_expiration_rule"
    enabled = true
    expiration {
      days = 7
    }
  }

  tags = {
    ENV = "dev"
    APP = "dbunloader"
  }

  lifecycle {
    prevent_destroy = true
  }
}