package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return passenger response entity"
    request {
        method 'GET'
        url '/api/passengers/1'
        headers {
            header('Content-Type', 'application/vnd.fraud.v1+json')
        }
    }
    response {
        status 200
        body("""
        {
                "id" : 1,
                "name": "Ivan1",
                "email": "ivan1@gmail.com",
                "phoneNumber": "+375291239871",
                "deleted": false
        }
                """
        )
    }
}