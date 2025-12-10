package com.example.thoughts_cleaning.common.vm

import com.example.thoughts_cleaning.common.ErrorCodeApp

/**
 * Created by seokang on 2022/09/15.
 */
interface ApiErrorHandler {
    fun exceptionGeneral(liveEvent: SingleLiveEvent<Exception>, e: SuniException.GeneralException) = run { liveEvent.postValue(e) }
    fun exceptionNoVts(liveEvent: SingleLiveEvent<Exception>, e: SuniException.NoVtsException) = run { liveEvent.postValue(e) }
    fun exceptionNeedAppUpdate(liveEvent: SingleLiveEvent<Exception>, e: SuniException.NeedAppUpdateException) = run { liveEvent.postValue(e) }
    fun exceptionInvalidSession(liveEvent: SingleLiveEvent<Exception>, e: SuniException.InvalidSessionException) = run { liveEvent.postValue(e) }
    fun exceptionInvalidLanguage(liveEvent: SingleLiveEvent<Exception>, e: SuniException.InvalidLanguageException) = run { liveEvent.postValue(e) }
    fun exceptionExpiredSession(liveEvent: SingleLiveEvent<Exception>, e: SuniException.ExpiredSessionException) = run { liveEvent.postValue(e) }
    fun exceptionNetwork(liveEvent: SingleLiveEvent<ErrorCodeApp?>) = run { liveEvent.postValue(null) }

    fun exceptionShopAlreadyOrder(liveEvent: SingleLiveEvent<Exception>, e: SuniException.ShopAlreadyOrderException) = run { liveEvent.postValue(e) }
    fun exceptionMustLoginAsOwner(liveEvent: SingleLiveEvent<Exception>, e: SuniException.MustLoginAsOwnerException) = run { liveEvent.postValue(e) }
    fun exceptionEventNotEnd(liveEvent: SingleLiveEvent<Exception>, e: SuniException.EventNotEndException) = run { liveEvent.postValue(e) }
    fun exceptionEventIsTooOld(liveEvent: SingleLiveEvent<Exception>, e: SuniException.EventIsTooOldException) = run { liveEvent.postValue(e) }

    fun exceptionFamilyAlready(liveEvent: SingleLiveEvent<Exception>, e: SuniException.FamilyAlreadyException) = run { liveEvent.postValue(e) }
    fun exceptionFollowExceedSearch(liveEvent: SingleLiveEvent<Exception>, e: SuniException.FollowExceedSearchException) = run { liveEvent.postValue(e) }
    fun exceptionFollowAlready(liveEvent: SingleLiveEvent<Exception>, e: SuniException.FollowAlreadyException) = run { liveEvent.postValue(e) }

    fun exceptionNanumAlreadyGiveRequest(liveEvent: SingleLiveEvent<Exception>, e: SuniException.NanumAlreadyGiveRequestException) = run { liveEvent.postValue(e) }
    fun exceptionNanumAlreadyBuyRequest(liveEvent: SingleLiveEvent<Exception>, e: SuniException.NanumAlreadyBuyRequestException) = run { liveEvent.postValue(e) }

    fun exceptionNoOnAirBroadcast(liveEvent: SingleLiveEvent<Exception>, e: SuniException.NoOnAirBroadcastException) = run { liveEvent.postValue(e) }
}