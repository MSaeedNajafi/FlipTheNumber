//
//  signupVc.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 08/12/20.
//

import UIKit
import Localize
import DropDown
import IQKeyboardManagerSwift
import Alamofire
import NVActivityIndicatorView



class signupVc: BaseViewController,UITextFieldDelegate {

    @IBOutlet weak var signup_btn: UIButton!
    @IBOutlet weak var signup_view: UIView!
    
    @IBOutlet weak var datepicker_view: UIView!
    @IBOutlet weak var email_textfield: UITextField!
    
    @IBOutlet weak var date_picker: UIDatePicker!
    @IBOutlet weak var password_textfield: UITextField!
    @IBOutlet weak var gender_textfield: UITextField!
    @IBOutlet weak var name_textfield: UITextField!
    @IBOutlet weak var bitrthday_textfield: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        signup_view.layer.cornerRadius = 15
        password_textfield.layer.cornerRadius = 5
        password_textfield.layer.borderWidth = 1
        email_textfield.layer.borderColor = UIColor.lightGray.cgColor
        password_textfield.layer.borderColor = UIColor.lightGray.cgColor
        gender_textfield.layer.borderColor = UIColor.lightGray.cgColor
        name_textfield.layer.borderColor = UIColor.lightGray.cgColor
        bitrthday_textfield.layer.borderColor = UIColor.lightGray.cgColor
        bitrthday_textfield.setLeftPaddingPoints(10)
        password_textfield.setLeftPaddingPoints(10)
        bitrthday_textfield.setLeftPaddingPoints(10)
       gender_textfield.setLeftPaddingPoints(10)
        email_textfield.setLeftPaddingPoints(10)
        name_textfield.setLeftPaddingPoints(10)
        email_textfield.font = UIFont.init(name:"Museo", size: 18)
        password_textfield.font = UIFont.init(name:"Museo", size: 18)
        bitrthday_textfield.font = UIFont.init(name:"Museo", size: 18)
        gender_textfield.font = UIFont.init(name:"Museo", size: 18)
        name_textfield.font = UIFont.init(name:"Museo", size: 18)
        signup_btn.titleLabel?.font =  UIFont(name: "Museo", size: 20)

        gender_textfield.layer.cornerRadius = 5
        gender_textfield.layer.borderWidth = 1

        name_textfield.layer.cornerRadius = 5
        name_textfield.layer.borderWidth = 1
        
        email_textfield.layer.cornerRadius = 5
        email_textfield.layer.borderWidth = 1

        bitrthday_textfield.layer.cornerRadius = 5
              bitrthday_textfield.layer.borderWidth = 1
        signup_btn.layer.cornerRadius = 5
        datepicker_view.isHidden = true

        // Do any additional setup after loading the view.
    }
    
    @IBAction func signup_btn(_ sender: Any) {
        
    if checkValidation() {
                         
                         Register()
                     }
                 }

    func isValidEmail(_ emailString: String) -> Bool {
        
        let emailRegex = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
        
        let emailTest = NSPredicate(format: "SELF MATCHES %@", emailRegex)
        
        return emailTest.evaluate(with: emailString)
    }
    
    func checkValidation() -> Bool {
        
        if (self.email_textfield.text ?? "").replacingOccurrences(of: " ", with: "") == "" {
              
             self.showAlertWithouTitle(message: "Please enter email")
                    
             return false
                }
        else if !self.isValidEmail(email_textfield
                                    .text!) {
            
             self.showAlertWithouTitle(message: "Please enter valid email")
            return false
        }
        
        else if (self.bitrthday_textfield.text ?? "").replacingOccurrences(of: " " , with: "") == "" {
            
             self.showAlertWithouTitle(message: "Please enter birth Date")
            
            return false
        }
        else if (self.name_textfield.text ?? "").replacingOccurrences(of: " " , with: "") == "" {

            
             self.showAlertWithouTitle(message: "Please enter Name")
            
            return false
        }
        else if (self.gender_textfield.text ?? "").replacingOccurrences(of: " " , with: "") == "" {
            
             self.showAlertWithouTitle(message: "Please enter Gender")
            
            return false
        }
     
        else if (self.password_textfield.text!.count) < 6 {
            
            self.showAlertWithouTitle(message: "Password length should be greater than 5")
            
            return false
        }else {
            
            return true
        }
    }
 func textFieldDidBeginEditing(_ textField: UITextField) {
            //delegate method
            datepicker_view.isHidden = false
            showDatePicker()

        }

        func textFieldShouldEndEditing(_ textField: UITextField) -> Bool {
            textField.resignFirstResponder()
    //delegate method
            return true
        }

        
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
        func showDatePicker(){
          //Formate Date
           // dbb_textfield.inputView = datePicker
            date_picker.datePickerMode = UIDatePicker.Mode.date

            let dateFormatter = DateFormatter()

                  dateFormatter.dateFormat = "dd MMMM yyyy"

            bitrthday_textfield.text = dateFormatter.string(from: date_picker.date)
               bitrthday_textfield.resignFirstResponder()
                  

         }

    @IBAction func skip_btn(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func donepickerview(_ sender: Any) {
            let formatter = DateFormatter()
                 formatter.dateFormat = "dd/MM/yyyy"
                 bitrthday_textfield.text = formatter.string(from: date_picker.date)
            datepicker_view.isHidden = true
        }
        

        @IBAction func cancelpickerview(_ sender: Any) {
             datepicker_view.isHidden = true

        }


  func Register()
  {
 
//      let Auth_header    = [
//                 "language" : keydata]
    let parameters: Parameters = [ "name" : name_textfield.text!, "password" : password_textfield.text!, "email" : email_textfield.text!,"device_type" : "ios","device_token" : "12345678","dob" : bitrthday_textfield.text!,"gender" :gender_textfield.text!,"type" : "user","mobile" : "123457","countryCode" : "+91"]
    print(parameters)

     AF.request (BaseUrl.register.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: nil).responseJSON
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

                                    if status == true
                                    {
                                        DispatchQueue.main.async {
                                            UserDefaults.standard.set(true, forKey: "isLogin")
                                            let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                                      let vc = storyboard.instantiateViewController(withIdentifier: "loginViewController") as! loginViewController
                                                              self.navigationController?.pushViewController(vc,
                                                       animated: true)
                                        }
                                    }
                                    else
                                    {
                                        let msg = JSON["message"] as! String
                                        let alert = UIAlertController(title: "Dice", message: msg, preferredStyle: UIAlertController.Style.alert)
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
}


extension UITextField {
func setLeftPaddingPoints(_ amount:CGFloat){
    let paddingView = UIView(frame: CGRect(x: 0, y: 0, width: amount, height: self.frame.size.height))
    self.leftView = paddingView
    self.leftViewMode = .always
}
}
