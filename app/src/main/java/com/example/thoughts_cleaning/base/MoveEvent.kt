package com.example.thoughts_cleaning.base

import com.example.thoughts_cleaning.views.main.SettingEvent
import com.example.thoughts_cleaning.views.start.LoginEvent

/**
 * Created by seokang on 2022/09/15.
 */

sealed class MoveEvent {
//    data class Dialog(val moveType: DialogEvent) : MoveEvent()
//
//
    data class Splash(val moveType: SplashEvent) : MoveEvent()
//    data class MasilBack(val moveType: MasilBackEvent) : MoveEvent()
//    data class Wearer(val moveType: WearerEvent) : MoveEvent()
//
//    data class CallPersonal(val moveType: CallPersonalEvent) : MoveEvent()
//
//    data class UserInfo(val moveType: UserInfoEvent) : MoveEvent()
//
//    data class UserInfoEdit(val moveType: UserInfoEditEvent) : MoveEvent()
//
//
//    data class GUserInfo(val moveType: GUserInfoEvent) : MoveEvent()
//
//    data class GUserInfoEdit(val moveType: GUserInfoEditEvent) : MoveEvent()
//
//
//    data class Guardian(val moveType: GuardianEvent) : MoveEvent()
//    data class Live(val moveType: LiveEvent) : MoveEvent()
//    data class RandomCall(val moveType: RandomCallEvent) : MoveEvent()
//    data class VideoChatting(val moveType: VideoChattingEvent) : MoveEvent()
//
//    data class Karaoke(val moveType: KaraokeEvent) : MoveEvent()
//
//    data class KaraokeHistory(val moveType: KaraokeHistoryEvent) : MoveEvent()
//    data class ContactSearch(val moveType: ContactSearchEvent) : MoveEvent()
//    data class ContactInfo(val moveType : ContactInfoEvent) : MoveEvent()
//
//    data class ContactMotion(val moveType: ContactMotionEvent) : MoveEvent()
//    data class ContactReport(val moveType: ContactReportEvent) : MoveEvent()
//    data class Privacy(val moveType: PrivacyEvent) : MoveEvent()
//    data class WearerNotice(val moveType: WearerNoticeEvent) : MoveEvent()
//    data class CommonLocation(val moveType: CommonLocationEvent) : MoveEvent()
//    data class ShopMain(val moveType: ShopMainEvent) : MoveEvent()
//    data class ShopCategory(val moveType: ShopCategoryEvent) : MoveEvent()
//    data class ShopCategoryDetail(val moveType: ShopCategoryDetailEvent) : MoveEvent()
//    data class FamilyList(val moveType: FamilyListEvent) : MoveEvent()
//    data class FamilyRequest(val moveType: FamilyRequestEvent) : MoveEvent()
//    data class FamilyDetail(val moveType: FamilyDetailEvent) : MoveEvent()
//    data class FamilySubscribe(val moveType: FamilySubscribeEvent) : MoveEvent()
//    data class AddressRequest(val moveType: AddressRequestEvent) : MoveEvent()
//
//    data class MasilStart(val moveType: MasilStartEvent): MoveEvent()
//    data class Permission(val moveType: PermissionEvent): MoveEvent()
    data class Login(val moveType: LoginEvent) : MoveEvent()
//
//
//    data class Register1(val moveType: Register1Event) : MoveEvent()
//
//    data class RegisterUserTypeSelectEvent(val moveType: kr.dnx.ble.android.touchcare.views.start.RegisterUserTypeSelectEvent) : MoveEvent()
//    data class PhoneInfo(val moveType: PhoneInfoEvent) : MoveEvent()
//    data class UserBasicInfo(val moveType: UserBasicInfoEvent) : MoveEvent()
//
    data class Setting(val moveType: SettingEvent) : MoveEvent()
//    data class SettingCareGiver(val moveType: SettingCareGiverEvent) : MoveEvent()
//    data class ServiceManage(val moveType: ServiceManageEvent): MoveEvent()
//    data class ServiceVolume(val moveType: ServiceVolumeEvent): MoveEvent()
//    data class TagManage(val moveType: TagManageEvent) : MoveEvent()
//    data class WatchManage(val moveType: WatchManageEvent) : MoveEvent()
//    data class EtcManage(val moveType: EtcManageEvent) : MoveEvent()
//    data class Notification(val moveType: NotificationEvent) : MoveEvent()
//
//    data class ShareMain(val moveType: ShareMainEvent) : MoveEvent()
//    data class ShareDetail(val moveType: ShareDetailEvent) : MoveEvent()
//
//    data class PaidFlow(val moveType: PaidFlowEvent) : MoveEvent()
//
//    data class DoctorFlow(val moveType: DoctorFlowEvent) : MoveEvent()
//    data class AutoviographyFlow(val moveType: AutoviographyFlowEvent) : MoveEvent()
//    data class QuizFlow(val moveType: QuizFlowEvent) : MoveEvent()

}
//
//enum class CallPersonalEvent { CALL }
//
enum class SplashEvent {
//    MAIN_WEARER,
//    MAIN_GUARDIAN,
                       //PERMISSION,
    MAIN, LOGIN,
    FINISH
}

//enum class MasilBackEvent {
//    BACK_BUTTON,
//    RIGHT_BUTTON,
//    TITLE_RIGHT_BUTTON,
//    TERMS_OF_USE
//}
//
//enum class LockScreenEvent {
//    EMERGENCY, POINT_BOX, SHOP, REFRESH, MAIN
//}
//
//enum class LiveEvent {
//    COMMON, LISTEN, SPEECH, EXIT, FINISH, OVERLAY
//}
//enum class RandomCallEvent {
//    COMMON, LISTEN, SPEECH, EXIT, OVERLAY, YES_BUTTON, NO_BUTTON
//}
//
//enum class VideoChattingEvent {
//    COMMON, LISTEN, SPEECH, EXIT, OVERLAY, YES_BUTTON, NO_BUTTON,
//    EDIT, EDIT_COMPLETE, PICTURE_DIALOG, EDIT_CLOSE
//
//
//}
//
//enum class KaraokeEvent {
//    COMMON, SPEECH, EXIT, OVERLAY, BLOCK
//}
//
//enum class KaraokeHistoryEvent {
//    PLAY, PAUSE
//}
//
//enum class GuardianEvent {
//    REPORT, BEHAVIOR, FITNESS, LOCATION, CONTACT_USER, PROFILE, SETTING, SEARCH, CONTACT, MOTION, SUNI_GUIDE, VOICE_RECORD,
//    MENU_CLOSE, PLAY_STORE, MENU_OPEN, MY_INFO, CONTACT_USER_SIZE_CHANGE, INTRO, START_EMERGENCY, SUCCESS_EMERGENCY, FAIL_EMERGENCY
//
//}
//
//enum class ContactSearchEvent { EXIT, BACKGROUND }
//
//enum class ContactInfoEvent {
//                            EXIT, UNJOIN, SUCCESS_UNJOIN, SUBSCRIBE, CALL
////    , EMERGENCY_START, EMERGENCY_STOP, EMERGENCY_FAIL, EMERGENCY_END
//}
//
//enum class ContactMotionEvent { EXIT, CLEAR }
//
//enum class ContactReportEvent { EXIT, DATE }
//
//enum class PrivacyEvent { EXIT }
//
//enum class CommonLocationEvent { CURRENT, LOCATION, PERMISSION, ADDRESS_TOAST, SAVE, SAVE_DIALOG, NEED_INPUT_ADDRESS, SUCCESS_POST_INFO }
//
//enum class ShopMainEvent { PRODUCT_BUY, SHOP_CATEGORY }
//
//enum class ShopCategoryEvent { PRODUCT_BUY_SUCCESS }
//
//enum class ShopCategoryDetailEvent { PRODUCT_BUY_ASK, PRODUCT_BUY_SUCCESS }
//
//enum class DialogEvent {
//    CONFIRM, CANCEL
//}
//
//
//
//enum class PaidFlowEvent {
//    CONNECT_FAIL, ACKNOWLEDGE_PURCHASE_FAIL, PURCHASES_USER_CANCELED, PURCHASES_ERROR, PRODUCT_LIST_FAIL, BILLING_FLOW_FAIL,
//    PLAN_ID_NOT_FOUND, COMPLETED_PURCHASE_FAIL, PAID_SUCCESS, PRODUCT_ID_NOT_FOUND,
//    NOT_EXPIRED_SUBSCRIBE, ALREADY_SUBSCRIBE_INFO
//}
//
//enum class DoctorFlowEvent {
//    COMMON, LISTEN, SPEECH, EXIT, OVERLAY, YES_BUTTON, NO_BUTTON,
//    EDIT, EDIT_COMPLETE, PICTURE_DIALOG, EDIT_CLOSE
//}
//enum class AutoviographyFlowEvent {
//    COMMON, LISTEN, SPEECH, EXIT, OVERLAY, YES_BUTTON, NO_BUTTON,
//    EDIT, EDIT_COMPLETE, PICTURE_DIALOG, EDIT_CLOSE
//}
//enum class QuizFlowEvent {
//    COMMON, LISTEN, SPEECH, EXIT, OVERLAY, YES_BUTTON, NO_BUTTON,
//    NEXT_PROBLEM, AGAIN_PROBLEM
//}



