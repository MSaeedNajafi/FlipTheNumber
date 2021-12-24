//
//  service.swift
//  Bunavid
//
//  Created by Murteza on 08/11/2019.
//  Copyright © 2019 Awais Malik. All rights reserved.
//

import Foundation
import Foundation
enum BaseUrl: String
{
    case register = "register"
    case login = "login"
    case  forget_pswd = "forget_pswd"
    case getregistrationdata = "getregistrationdata"
    case edit_profile = "edit_profile"
    case GetHomePageDetails = "GetHomePageDetails"
    case social_login = "social_login"
    case user_list = "user_list"
    case sendrequest = "sendrequest"
    case myrequest = "myrequest"
    case accept_request = "accept_request"

    case invite = "getfriend"



    
    

   




    


    
 //   case getSignUpData = "getregistrationdata"
    
   //https://buenavid.com/buenavid/api/
    func toURL () -> String
    {
        return "http://141.136.36.151/api/" + self.rawValue
    }
    

    
}
