package com.example.thoughts_cleaning.common.vm

/**
 * Created by SeoKang on 2022-09-15.
 */

sealed class SuniException(errorMessage: String) : Exception(errorMessage) {
    class GeneralException(errorMessage: String) : SuniException(errorMessage)
    class InvalidSessionException(errorMessage: String) : SuniException(errorMessage)
    class InvalidLanguageException(errorMessage: String) : SuniException(errorMessage)
    class ExpiredSessionException(errorMessage: String) : SuniException(errorMessage)
    class NoVtsException(errorMessage: String) : SuniException(errorMessage)
    class NeedAppUpdateException(errorMessage: String) : SuniException(errorMessage)
    class ShopAlreadyOrderException(errorMessage: String) : SuniException(errorMessage)
    class MustLoginAsOwnerException(errorMessage: String) : SuniException(errorMessage)

    class EventNotEndException(errorMessage: String) : SuniException(errorMessage)
    class EventIsTooOldException(errorMessage: String) : SuniException(errorMessage)

    class FamilyAlreadyException(errorMessage: String) : SuniException(errorMessage)
    class FollowExceedSearchException(errorMessage: String) : SuniException(errorMessage)
    class FollowAlreadyException(errorMessage: String) : SuniException(errorMessage)

    class NanumAlreadyGiveRequestException(errorMessage: String) : SuniException(errorMessage)
    class NanumAlreadyBuyRequestException(errorMessage: String) : SuniException(errorMessage)

    class NoOnAirBroadcastException(errorMessage: String) : SuniException(errorMessage)
}

