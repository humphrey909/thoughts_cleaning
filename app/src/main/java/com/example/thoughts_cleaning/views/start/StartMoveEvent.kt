package com.example.thoughts_cleaning.views.start

/**
 * Created by seokang on 2022/09/15.
 */



enum class MasilStartEvent {
    CLOSE_BUTTON, NEXT_BUTTON, HIDE_KEYBOARD
}

enum class PermissionEvent {
    SUCCESS_LOGOUT, SUCCESS_LOGOUT_WITH_MESSAGE, COMPLETE_BATTERY_PERMISSION, COMPLETE_ALARM_PERMISSION
}

enum class LoginEvent {
    NAVER_LOGIN, KAKAO_LOGIN

//    MAIN_WEARER, INTEGRATED_USER, PERMISSION,
//    MAIN_GUARDIAN,
//    DEBUG, HIDE_KEYBOARD,
//    LOGO_IMAGE, LOGIN_BUTTON, FIND_BUTTON, REGISTER_BUTTON,
//    MUST_LOGIN_AS_OWNER,
//
//    SOCIAL_REGISTER,
//    REGISTER_FAIL,
//    NETWORK_FAIL
}

enum class Register1Event {
    EXIST_USER
}

enum class RegisterUserTypeSelectEvent {
    OWNER_DISABLE, GUARDIAN_DISABLE
}

enum class PhoneInfoEvent{
    COUNTRY_DIALOG,
    SUCCESS_CERT_REQUEST,
    SUCCESS_CERT_VERIFY
}

enum class UserBasicInfoEvent {
    DATE_DIALOG,
    SUCCESS
}