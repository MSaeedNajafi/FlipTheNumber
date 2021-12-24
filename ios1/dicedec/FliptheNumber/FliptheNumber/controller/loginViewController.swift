//
//  loginViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 05/12/20.
//

import UIKit
import GoogleSignIn
import FBSDKCoreKit
import FBSDKLoginKit
import Alamofire
import IQKeyboardManagerSwift


class loginViewController: BaseViewController,UITextFieldDelegate, GIDSignInDelegate {
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error!) {
         if let error = error {
             if (error as NSError).code == GIDSignInErrorCode.hasNoAuthInKeychain.rawValue {
                 print("The user has not signed in before or they have since signed out.")
             } else {
                 print("\(error.localizedDescription)")
             }
             return
         }

         let idToken = user.authentication.idToken ?? ""
         let givenName = user.profile.givenName ?? ""
         let familyName = user.profile.familyName ?? ""
         let email = user.profile.email ?? ("\(givenName)@gmail.com")
         
         print(user.userID!)
         print(idToken)
         print(givenName)
         print(familyName)
         print(email)
        socialname = givenName
              socialemail = email
        socialapi(value: "gmail")

     }
     
    var socialname: String = ""
    var socialemail: String = ""
    @IBOutlet weak var forgot_view: UIView!
    @IBOutlet weak var login_btn: UIButton!
    @IBOutlet weak var login_view: UIView!
    @IBOutlet weak var password_textfield: UITextField!
        @IBOutlet weak var forgot_emailtextfield: UITextField!
     @IBOutlet weak var email_textField: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
      
        email_textField.layer.cornerRadius = 5
        login_btn.layer.cornerRadius = 5

        email_textField.layer.borderWidth = 1
        login_view.layer.cornerRadius = 15

        email_textField.layer.borderColor = UIColor.lightGray.cgColor

       email_textField.setLeftPaddingPoints(10)
       password_textfield.setLeftPaddingPoints(10)
        
        password_textfield.layer.borderWidth = 1
        forgot_emailtextfield.font = UIFont.init(name:"Museo", size: 18)
        email_textField.font = UIFont.init(name:"Museo", size: 18)
        password_textfield.font = UIFont.init(name:"Museo", size: 18)
        login_btn.titleLabel?.font =  UIFont(name: "Museo", size: 18)

        password_textfield.layer.cornerRadius = 5
        password_textfield.layer.borderColor = UIColor.lightGray.cgColor
        GIDSignIn.sharedInstance().delegate = self
                     GIDSignIn.sharedInstance()?.presentingViewController = self
        self.navigationController?.isNavigationBarHidden = true

        forgot_view.isHidden = true
        // Do any additional setup after loading the view.
    }
    @IBAction func login_btn(_ sender: Any)
    {
        if checkValidation()
        {
          loginApi()
      }
        
    }
    
    @IBAction func forgot_password(_ sender: Any) {
        forgot_view.isHidden = false
    }
    @IBAction func forgot_submitbtn(_ sender: Any) {
        forgotApi()


    }
    @IBAction func fb_btn(_ sender: Any) {
        let fbLoginManager : LoginManager = LoginManager()
        //   fbLoginManager.loginBehavior = FBSDKLoginBehavior.web

        fbLoginManager.logIn(permissions: ["email"], from: self) { (result, error) in
               if (error == nil){
                let fbloginresult : LoginManagerLoginResult = result!
                   if fbloginresult.grantedPermissions != nil {
                       if(fbloginresult.grantedPermissions.contains("email"))
                       {
                           self.getFBUserData()
                           fbLoginManager.logOut()

                       }
                   }
               }
           }
        
        
    }
    func getFBUserData(){
        let token = AccessToken.current?.tokenString
            let params = ["fields": "first_name, last_name, email"]
            let graphRequest = GraphRequest(graphPath: "me", parameters: params, tokenString: token, version: nil, httpMethod: .get)
            graphRequest.start { (connection, result, error) in

                if let err = error {
                    print("Facebook graph request error: \(err)")
                } else {
                    print("Facebook graph request successful!")

                    guard let json = result as? NSDictionary else { return }
                    
                     let email = json["email"] as? String
                    print("\(String(describing: email))")
                    
                    let firstName = json["first_name"] as? String
                    print("\(String(describing: firstName))")
                
                     let lastName = json["last_name"] as? String
                    print("\(String(describing: lastName))")
                    
                     let id = json["id"] as? String
                    print("\(String(describing: id))")
                    
                    self.socialname = firstName!
                    print(self.socialname)
                    if self.socialemail.count == 0
                    {
                        self.socialemail = ""
                    }
    else

                    {
                        self.socialname = email!

                    }
                    self.socialapi(value: "fb")



                }
        }
        }
    func socialapi(value: String)
         {
        
     
             let parameters: Parameters = [ "email" : socialemail, "type" : value, "name" : socialname,"device_type" : "ios","device_token" : "12345678"]
         ActivityIndicator.shared.showLoadingIndicator(text: "Loading..")
            print(parameters)


            AF.request (BaseUrl.social_login.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: nil).responseJSON
                       {

                   
                     response in
                       switch response.result {
                                     case .success(let value):
                                       print(response.value!)
                                        if let JSON = value as? [String: Any]
                                        {
                                                              let status = JSON["status"] as! Bool
                                                              print(status)
                                           let responseInfo = JSON["data"] as! NSArray
                                           print(responseInfo)



                                           print(responseInfo.value(forKey: "token") as Any)

                                           if status == true
                                           {
                                               DispatchQueue.main.async {

                                                   ActivityIndicator.shared.hideLoadingIndicator()

                                                   var dataArray =  NSArray()
                                                   dataArray = responseInfo
                                                  UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "token"), forKey: "token")
                                                UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "user_id"), forKey: "user_id")
                                                  UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "email"), forKey: "email")
                                               UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "total_token"), forKey: "total_token")
                                                UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "total_joker"), forKey: "total_joker")
                                                   print("\(UserDefaults.standard.value(forKey: "user_id")!)")
                                                   UserDefaults.standard.set(true, forKey: "islogin")
                                                   UserDefaults.standard.synchronize()

                                                   ActivityIndicator.shared.hideLoadingIndicator()

                                                   let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                                   
                                                    let vc = storyboard.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
                                                   vc.arrayData = dataArray
                                                   vc.value = "1"
                                                    self.navigationController?.pushViewController(vc,
                                                                                                  animated: true)
                                                   
                                               }
                                           }
                                           else
                                           {
                                               let msg = JSON["message"] as! String
                                               let alert = UIAlertController(title: "Color Picker", message: msg, preferredStyle: UIAlertController.Style.alert)
                                               alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                                             self.present(alert, animated: true)

                                               ActivityIndicator.shared.hideLoadingIndicator()

                                           }
                                                          }
                                       
                                         //if response.data[sta]
                                         

                                     case .failure(let error):
                                       print(error)

                                       break

                                     }
                     }
         
              
                  
              }
    
    @IBAction func gmailbtn(_ sender: Any) {
        GIDSignIn.sharedInstance().signOut()
                       GIDSignIn.sharedInstance().signIn()
    }
    @IBAction func signup_btn(_ sender: Any) {

        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "signupVc") as! signupVc
        self.navigationController?.pushViewController(vc,
         animated: true)
        
     
    }
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    @IBAction func skip_btn(_ sender: Any) {
    }
    func isValidEmail(_ emailString: String) -> Bool {
        
        let emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
        
        let emailTest = NSPredicate(format: "SELF MATCHES %@", emailRegex)
        
        return emailTest.evaluate(with: emailString)
    }
    func forgotApi()
        {
          
        //      let Auth_header    = [
        //                 "language" : keydata]
              let parameters: Parameters = [ "email" : forgot_emailtextfield.text!]
            ActivityIndicator.shared.showLoadingIndicator(text: "Loading..")
print(parameters)
               AF.request (BaseUrl.forget_pswd.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: nil).responseJSON
                {
            
              response in
                switch response.result {
                              case .success(let value):
                                print(response.value!)
                                let jsonData = value
                                               print(jsonData)
                                ActivityIndicator.shared.hideLoadingIndicator()

                                 if let JSON = value as? [String: AnyObject] {
                                                       let status = JSON["status"] as! Bool
                                                       print(status)
                                    let responseInfo = JSON["data"] as! NSArray
                                    print(responseInfo)
                                    if status == true
                                    {
                                        DispatchQueue.main.async {
                                         
                                            self.forgot_view.isHidden = true

                                         
                                        }
                                    }
                                    else
                                    {
                                        ActivityIndicator.shared.hideLoadingIndicator()

                                        let msg = JSON["message"] as! String
                                        let alert = UIAlertController(title: "Color Picker", message: msg, preferredStyle: UIAlertController.Style.alert)
                                        alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                                      self.present(alert, animated: true)

                                        ActivityIndicator.shared.hideLoadingIndicator()

                                    }
                                                   }
                                
                                  //if response.data[sta]
                                  

                              case .failure(let error):
                                print(error)

                                break

                              }
              }
        }
    func loginApi()
        {
          
        //      let Auth_header    = [
        //                 "language" : keydata]
              let parameters: Parameters = [ "email" : email_textField.text!, "password" : password_textfield.text!]
            ActivityIndicator.shared.showLoadingIndicator(text: "Loading..")

               AF.request (BaseUrl.login.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: nil).responseJSON
                {

            
              response in
                switch response.result {
                              case .success(let value):
                                print(response.value!)
                                let jsonData = value
                                               print(jsonData)
                                 if let JSON = value as? [String: AnyObject] {
                                                       let status = JSON["status"] as! Bool
                                                       print(status)
                                    let responseInfo = JSON["data"] as! NSArray
                                    print(responseInfo)
                                    if status == true
                                    {
                                        DispatchQueue.main.async {
                                         UserDefaults.standard.setValue(responseInfo, forKey: "userdata")
                                            UserDefaults.standard.set(true, forKey: "islogin")
                                            UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "total_token"), forKey: "total_token")
                                               UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "total_joker"), forKey: "total_joker")
                                               UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "token"), forKey: "token")
                                               UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "image"), forKey: "image")
                                            UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "user_id"), forKey: "user_id")
                                            print("\(UserDefaults.standard.value(forKey: "user_id")!)")
                                            print("\(UserDefaults.standard.value(forKey: "total_token")!)")
                                            print("\(UserDefaults.standard.value(forKey: "total_joker")!)")
                                                print("\(UserDefaults.standard.value(forKey: "token")!)")



    //                                        let tokens = UserDefaults.standard.string(forKey: "token") ?? ""
    //                                        print(tokens)

                                            var dataArray =  NSArray()
                                            dataArray = responseInfo
                                            print(dataArray)
                                           

                                            ActivityIndicator.shared.hideLoadingIndicator()
                                            let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                            
                                             let vc = storyboard.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
                                            vc.arrayData = dataArray
                                             self.navigationController?.pushViewController(vc,
                                                                                           animated: true)
                                        }
                                    }
                                    else
                                    {
                                        let msg = JSON["message"] as! String
                                        let alert = UIAlertController(title: "Color Picker", message: msg, preferredStyle: UIAlertController.Style.alert)
                                        alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                                      self.present(alert, animated: true)

                                        ActivityIndicator.shared.hideLoadingIndicator()

                                    }
                                                   }
                                
                                  //if response.data[sta]
                                  

                              case .failure(let error):
                                print(error)

                                break

                              }
              }
        }
    func checkValidation() -> Bool {
        
       if (email_textField.text ?? "").replacingOccurrences(of: " ", with: "") == "" {
             
            self.showAlertWithouTitle(message: "Please enter email")
                   
            return false
               }
        else if !self.isValidEmail(email_textField.text!) {
            
            //Users/gaurav/Desktop/RenuDiceProject/FliptheNumber/Pods
            self.showAlertWithouTitle(message: "Please enter valid email")
            return false
        }
        
        else if (self.password_textfield.text ?? "").replacingOccurrences(of: " " , with: "") == "" {
            
             self.showAlertWithouTitle(message: "Please enter password")
            
            return false
        }
        else if (self.password_textfield.text!.count) < 6 {
            
            self.showAlertWithouTitle(message: "Password length should be greater than 5")
            
            return false
        }else {
            
            return true
        }
    }
    
}



