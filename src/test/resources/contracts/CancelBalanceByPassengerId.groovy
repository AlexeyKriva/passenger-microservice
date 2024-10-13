//package contracts
//
//import org.springframework.cloud.contract.spec.Contract
//
//Contract.make {
//    description "Should return passenger account response entity"
//    request {
//        method 'PUT'
//        url '/api/passenger/account/3/cancel'
//        headers {
//            header('Content-Type', 'application/vnd.fraud.v1+json')
//        }
//        body("""
//            {
//                "balance": 0,
//                "currency": "BYN"
//            }
//        """
//        )
//    }
//    response {
//        status 200
//        body("""
//            {
//                "id" : 3,
//                "passenger" : {
//                    "id" : 3,
//                    "name": "Ivan3",
//                    "email": "ivan3@gmail.com",
//                    "phoneNumber": "+375291239873",
//                    "deleted": false
//                },
//                "balance" : 0,
//                "currency" : "BYN"
//            }
//        """
//        )
//    }
//}
