package mn.delivery.system.model.systemconfig.enums;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tergel
 */
public enum SystemCronType {

    // ---------- SHARED CRON ----------

    // airdrop
    AIR_DROP_STATUS, // airdrop-н төлөв өөрчлөх
    AIR_DROP_REWARD, // airdrop-н шагналыг шилжүүлэх
    AIR_DROP_TASK, // airdrop-н даалгавар шалгах

    // auction
    AUCTION_CHOOSE_WINNER, // дуудлага худалдааны ялагч сонгох

    // dao question
    DAO_QUESTION_STATUS,

    // egg hatch
    EGG_HATCH_FINISH, // egg hatch дуусгах
    FUSION_FINISH, // fusion дуусгах

    // easy buy
    EASY_BUY_PROCESS, // easy buy

    // egg hatch
    EVENT_STATUS, // event төлөв өөрчлөх

    KYC_CHECK, // pending KYC хүсэлтүүдийг шалгах

    // multi buy
    MULTI_BUY_EMAIL, // multi buy email

    // superDrop
    SUPER_DROP_STATUS, // superDrop-н төлөв өөрчлөх
    SUPER_DROP_REWARD, // superDrop-н шагналыг шилжүүлэх

    // push notification
    PUSH_NOTIFICATION_SEND,

    // email request
    EMAIL_SEND,

    // whitelist
    WHITELIST_STATUS, // feature-н төлөв өөрчлөх
    WHITELIST_TASK, // feature-н даалгавар шалгах

    // ---------- PAYMENT CRON ----------

    // payment
    PAYMENT_CONFIRM, // payment баталгаажуулах

    // transaction fiat
    STATEMENT_KHAN, // хаан банкны дансны хуулга болон дансны үлдэгдэл авах
    STATEMENT_GOLOMT, // голомт банкны дансны хуулга болон дансны үлдэгдэл авах
    STATEMENT_TDB, // худалдаа хөгжлийн банкны дансны хуулга болон дансны үлдэгдэл авах

    // fiat transaction
    DEPOSIT_FIAT_CONFIRM, // валютын орлогыг баталгаажуулах /MNT одоохондоо/
    WITHDRAW_FIAT_CONFIRM, // валютын зарлагыг баталгаажуулах /MNT одоохондоо/
    WITHDRAW_FIAT_KHAN, // хаан банкаар шилжүүлэг хийх
    WITHDRAW_FIAT_GOLOMT, // голомт банкаар шилжүүлэг хийх
    WITHDRAW_FIAT_TDB, // голомт банкаар шилжүүлэг хийх

    // ---------- CRYPTO CRON ----------

    // crypto
    DEPOSIT_POLYGON_NFT, // polygon дээрх NFT smart contract чагнах

    DEPOSIT_BNB,
    DEPOSIT_COREX,
    DEPOSIT_ETH,

    WITHDRAW_BNB,
    WITHDRAW_COREX,
    WITHDRAW_ETH,
    ;

    public static List<SystemCronType> getPaymentCronTypes() {
        return Arrays.asList(
                STATEMENT_KHAN,
                WITHDRAW_FIAT_KHAN,
                DEPOSIT_FIAT_CONFIRM,
                WITHDRAW_FIAT_CONFIRM,
                PAYMENT_CONFIRM
        );
    }

    public static List<SystemCronType> getPaymentGolomtCronTypes() {
        return Arrays.asList(
                STATEMENT_GOLOMT,
                WITHDRAW_FIAT_GOLOMT
        );
    }

    public static List<SystemCronType> getPaymentTdbCronTypes() {
        return Arrays.asList(
                STATEMENT_TDB,
                WITHDRAW_FIAT_TDB
        );
    }

    public static List<SystemCronType> getSharedCronTypes() {
        return Arrays.asList(
                AIR_DROP_STATUS,
                AIR_DROP_REWARD,
                AUCTION_CHOOSE_WINNER,
                DAO_QUESTION_STATUS,
                EGG_HATCH_FINISH,
                FUSION_FINISH,
                EASY_BUY_PROCESS,
                EVENT_STATUS,
                MULTI_BUY_EMAIL,
                SUPER_DROP_STATUS,
                SUPER_DROP_REWARD,
                PUSH_NOTIFICATION_SEND,
                EMAIL_SEND,
                WHITELIST_STATUS,
                WHITELIST_TASK
        );
    }

    public static SystemCronType fromString(String value) {
        try {
            return SystemCronType.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
