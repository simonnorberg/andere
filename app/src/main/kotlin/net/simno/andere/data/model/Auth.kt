package net.simno.andere.data.model

data class Auth(val access_token: String,
                val token_type: String,
                val device_id: String,
                val expires_in: Long,
                val scope: String)
