package contracts

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "Should return passenger account response entity"
    request {
        method 'PUT'
        url '/api/passengers/2/accounts/up'
        headers {
            header('Content-Type', 'application/vnd.fraud.v1+json')
        }
        body("""
            {
                "balance": 1000.5,
                "currency": "BYN"
            }
        """
        )
    }
    response {
        status 200
        body("""
            {
                "id" : 2,
                "passenger" : {
                    "id" : 2,
                    "name": "Ivan2",
                    "email": "ivan2@gmail.com",
                    "phoneNumber": "+375291239872",
                    "deleted": false
                },
                "balance" : 1000.5,
                "currency" : "BYN"
            }
        """
        )
    }
}
