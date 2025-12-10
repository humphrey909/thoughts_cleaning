package com.example.thoughts_cleaning.common

import com.example.thoughts_cleaning.R

enum class ErrorCodeApp(val resourceId: Int) {
     ID_EMPTY(R.string.error_id_empty),
     ID_LENGTH(R.string.error_id_length),
     ID_FIRST_ONLY_WORD(R.string.error_id_fist_only_word),
     ID_NOT_SELECTED(R.string.error_id_not_selected),
     PASSWORD_EMPTY(R.string.error_password_empty),
     PASSWORD_LENGTH(R.string.error_password_length),
     @Deprecated("패스워드 재입력칸 사라짐")
     PASSWORD_NOT_EQUAL(R.string.error_password_not_equal),
     PERMISSION_DENIED(R.string.error_permission_denied),
     PRIVACY_NOT_AGREE(R.string.error_privacy_not_agree),
     CERT_NOT_REQUEST(R.string.error_cert_not_request),
     CERT_EMPTY(R.string.error_cert_empty),
     USER_TYPE_EMPTY(R.string.error_user_type_empty),
     NAME_EMPTY(R.string.error_name_empty),
     GENDER_EMPTY(R.string.error_gender_empty),
     BIRTH_EMPTY(R.string.error_birth_empty),
     BIRTH_NOT_ADULT(R.string.error_birth_not_adult),
     ADDRESS_EMPTY(R.string.error_no_home_address),
     PHONE_NO_EMPTY(R.string.error_phone_no_empty),

     DEFAULT_ERROR(R.string.error_editable),
     DFU_DOWNLOAD_ERROR(R.string.dfu_file_download_fail),

     BLUETOOTH_NOT_SUPPORT(R.string.error_ble_not_support),
     SESSION_INVALID(R.string.error_session_invalid)

//     DATE_FUTURE(R.string.error_future_date),
//     VOICE_PERMISSION_DENIED(R.string.error_permission_voice_denied),
//     BLUETOOTH_OFF(0)
}