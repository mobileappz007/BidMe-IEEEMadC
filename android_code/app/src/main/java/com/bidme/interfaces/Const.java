package com.bidme.interfaces;

public interface Const {

    String APP_NAME = "Bidme";
    String APP_PACKAGE = "com.bidme";
    //String BASE_URL = "http://52.77.32.209:7016/api/";
/*
    String BASE_URL = "http://139.59.45.232:7016/api/";
*/
    String BASE_URL = "http://139.59.45.232:7017/api/";
    String PRIVACY_URL = "http://139.59.45.232/getrid/privacy.html";
    String FQ_URL = "http://139.59.45.232/getrid/faq.html";
    String TERMS_COND_URL = "http://139.59.45.232/getrid/term.html";


    /*API's*/
    String GET_ALL_ADVERTISE = "getAllAdvertise";
    String GET_ALL_AUCTION = "getAllAuctions";
    String GET_ALL_CATEGORY = "getAllCategory";
    String GET_ALL_Auctions = "getAllAuctions";
    String GET_SINGLE_AUNCTION = "getSingleAuction";
    String GET_All_BIDS = "getAllBid";
    String GET_MY_BID = "getMyBid";
    String ADD_BID = "addBid";
    String CHECKUSER = "checkUser";
    String GET_MY_AUNCTION = "getMyAuction";
    String GET_MYFAV = "getMyFavourite";
    String GET_MYUNFAV = "unFavourite";
    String UPDATE_AUNCTION = "update_auction";
    String BLOCK_USER = "blockUserBid";
    String UnBlockUser = "unBlockUserBid";
    String DELETE_AUNTION = "delete_auction";
    String DELETE_BID = "delete_bid";
    String UPDATE_BID = "update_bid";
    String GET_ALL_SUPPORT = "getAllSupport";
    String GET_SUP_TITLE = "sup_title";
    String GET_SUP_DESC = "sup_desc";
    String ADD_SUPPRT = "addSupport";
    String CAT_TITLE = "cat_title";
    String RemaingTime = "remaining_time";
    String PRO_PRICE = "price";
    String DELETE_BID_user = "delete_bid";
    String ADD_SUBSCRIBE = "addsubscription";
    String GET_SUB_HISTORY = "subscription_history";
    String VIEW_PROFILE = "getProfile";
    String GET_ALL_SEARCH = "getAllSearchAuction";
    String SEARCH = "search";
    String ADD_RATING = "addRating";
    String GET_ALL_RATING = "getAllRating";
    String GET_CHAT_HISTORY = "getChatHistory";
    String SENDER_ID = "sender_user_pub_id";
    String RECEIVER_ID = "receiver_user_pub_id";
    String SENDER_NAME = "sender_name";
    String SEND_MESSAGE = "sendMessage";
    String GET_SINGLE_CHAT_HISTORY = "getSingleChatHistory";
    String RECEIVER_NAME = "user_name";
    String CURRENT_SUB_HISTORY = "current_subscription";
    String BRAND_NAME = "current_subscription";
    String MODEL_NAME = "current_subscription";


    String UPDATE_PROFILE = "updateProfile";

    String GET_All_SUB_CATEGORY = "getSubAllCategory";
    String GET_All_BRANDS = "getAllBrands";
    String GET_All_MODEL = "getAllModels";
    String GET_All_SUBSCRIPTION_PACKAGE = "GetAllSubsPackage";
    String GET_NOTIFICATION = "getNotifications";
    String GET_FAVROUITE = "addFavourite";
    String REMOVE_IMAGE = "remove_image";
    String REMOVE_GALLERY = "remove";
    String NOTIFICATION_DTO = "notification_dto";

    String SIGN_UP = "register";
    String SIGN_IN = "login";
    String GET_BUYING_CHAT = "getBuyingChat";
    String GET_SELLING_CHAT = "getSellingChat";
    String FORGOT_PASSWORD = "ForgotPassword";
    String ADD_AUCTION = "addAuction";
    String ADD_ADVERTISE = "addAdvertise";
    String ADD_CATEGORY = "cat_id";
    String Pro_pub_id = "pro_pub_id";
    String WinnerDTO = "winnerDTO";
    String BID_PUB_ID = "bid_pub_id";
    String PROFIL_PIC = "ivProfilePic";
    String CITY = "city";
    String Town = "town";
    String COUNTRY = "country";
    String RECEIVER_IMAGE = "user_image";
    String ADD_IMAGES = "addImageAuction";

    /*Application Params*/
    String LATITUDE = "latitude";
    String MCURRENTLOCATION = "latitude";
    String LONGITUDE = "longitude";
    String USER_DTO = "user_dto";
    String IS_REGISTERED = "is_registered";
    String AUCTION_DTO = "auction_dto";
    String MyChatsDTO = "my_chat_dto";
    String GET_FILTER_AUNCTION = "getAllAuctions";

    String GET_CAT_ID = "cat_id";
    String GET_BRAND_ID = "brand_id";
    String GET_SUB_CAT_ID = "sub_cat_id";
    String DTO = "dto";
    String BIDPRICE = "price";
    //ADD_AUTION API
    String TITLE = "title";
    String PRICE = "price";
    String ADDRESS = "address";
    String DESCRIPTION = "description";
    String SHORT_DESCRIPTION = "sort_description";
    String IMAGE = "image";
    String S_Date = "s_date";
    String E_DATE = "e_date";
    String NO_OF_OWNER = "no_of_owner";
    String INSURED = "insured";
    String MODEL_ID = "model_id";
    String MAX_ID = "max_price";
    String MIN_ID = "min_price";
    String DISTANCE_ID = "distance";
    String POSTCODE = "postcode";
    String THREAD_ID = "getThreadId";
    String THREAD = "thread_id";
    String ID = "id";
    String FLAG = "flag";


    /*SignUp Params*/
    String EMAIL = "email";
    String PASSWORD = "password";
    String NAME = "name";
    String FIRST_NAME = "first_name";
    String LAST_NAME = "last_name";
    String ZIP = "zip";
    String TOWN = "town";
    String DEVICE_TOKEN = "device_token";
    String MOBILE_NO = "mobile_no";
    String DEVICE_TYPE = "device_type";
    String COUNTRY_CODE = "country_code";
    String GENDER = "gender";


    /*Change Password Params*/
    String USER_PUB_ID = "user_pub_id";
    String BLOCK_USER_PUB_ID = "block_user_pub_id";
    String OLD_PASSWORD = "old_password";
    String NEW_PASSWORD = "new_password";
    String C_PASSWORD = "changePassword";
    String IMAGE_URI_CAMERA = "IMAGE_URI_CAMERA";
    String MY_AUCTIONDTO = "myAuctiondto";

    /*subscription*/
    String PACKAGE_PUB_ID = "package_pub_id";


    String PACKAGE_NAME = "name";
    String DISCOUNT = "discount";
    String TOTAL_PRICE = "total_price";
    String FINAL_PRICE = "final_price";
    String TAX = "tax";
    String START_DATE = "start_date";
    String END_DATE = "end_date";


    String STAR = "star";
    String COMMENT = "comment";
    String MESSAGE = "message";
    String BROADCAST = "chat";
    String ALL_CHAT = "all_chat";
    String INDEX = "index";
    String WON_INDEX = "won_index";
    String WON_BID = "allWonAuction";

    //Chat type
    String CHAT_TYPE = "10001";
    String BID_TYPE = "10003";
    String WIN_BID_TYPE = "10005";
    String ADMIN_NOTIFICATION_TYPE = "10004";
    String BID_FAV_MESSAGE = "10006";
    String RESOLVED_ISSUE = "10007";
    String GUEST_USER_PUB_ID = "dnhx48zk1uo0zbj";
}
