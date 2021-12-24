//
//  ProfileViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 08/12/20.
//

import UIKit
import Alamofire

class ProfileViewController: UIViewController,UIImagePickerControllerDelegate,UINavigationControllerDelegate {
  var token: String = ""
    var id: Int = 0
    var value = ""


    @IBOutlet weak var user_image: UIImageView!
    @IBOutlet weak var diamond_lbl: UILabel!
    @IBOutlet weak var dollar: UILabel!
    @IBOutlet weak var Name_textfield: UITextField!
    @IBOutlet weak var joker: UILabel!
    @IBOutlet weak var email_textfield: UITextField!
    @IBOutlet weak var password_textfield: UITextField!
    @IBOutlet weak var editprofile_btn: UIButton!
    var arrayData = NSArray()

    override func viewDidLoad() {
        super.viewDidLoad()
print(arrayData)
        if value == "1"
        {
            let data = arrayData[0] as? NSDictionary

            
            id = (data?.value(forKey: "user_id") ?? 0 ) as! Int

                    dollar.text! = ((arrayData[0] as AnyObject).value(forKey: "total_token") as? String)!
         let imageURL:URL = URL(string: (((data?.value(forKey: "image") ?? 0 ) as? String)!))!
                           let dataimage = NSData(contentsOf: imageURL)
                           user_image.image = UIImage(data: dataimage! as Data)
            user_image!.layer.cornerRadius = user_image!.frame.height/2
            user_image!.layer.masksToBounds = false
            user_image!.clipsToBounds = true
            user_image!.contentMode = UIView.ContentMode.scaleAspectFill
//               user_image.layer.cornerRadius = user_image.frame.wid/2 //This will change with corners of image and height/2 will make this circle shape
//               user_image.clipsToBounds = true
                    
                    dollar.text! = ((data?.value(forKey: "total_token") ?? 0 )  as AnyObject).description
                    print(dollar.text as Any)
                    joker.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
                    print(joker.text as Any)
            Name_textfield.text! = ((data?.value(forKey: "name") ?? 0 )  as AnyObject).description
            email_textfield.text! = ((data?.value(forKey: "email") ?? 0 )  as AnyObject).description

            joker.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
            print(joker.text as Any)
        }
        else
        {

        print(UserDefaults.standard.value(forKey:"total_token") ?? 0)
        token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
        print(token)
        id = (UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as! Int
        joker.text = (UserDefaults.standard.value(forKey: "total_joker") ?? 0 ) as? String
            print(joker.text as Any)
        dollar.text = (UserDefaults.standard.value(forKey: "total_token") ?? 0 ) as? String
            Name_textfield.text = (UserDefaults.standard.value(forKey: "name") ?? 0 ) as? String
                       print(joker.text as Any)
                   email_textfield.text = (UserDefaults.standard.value(forKey: "total_token") ?? 0 ) as? String
            let imageURL:URL = URL(string: (((UserDefaults.standard.value(forKey: "image") ?? 0 ) as? String)!))!
                let dataimage = NSData(contentsOf: imageURL)
                user_image.image = UIImage(data: dataimage! as Data)
               user_image!.layer.cornerRadius = user_image!.frame.height/2
                     user_image!.layer.masksToBounds = false
                     user_image!.clipsToBounds = true
                     user_image!.contentMode = UIView.ContentMode.scaleAspectFill
                            
        }
         
        password_textfield.layer.cornerRadius = 5
        Name_textfield.layer.cornerRadius = 5
        email_textfield.layer.cornerRadius = 5
        editprofile_btn.layer.cornerRadius = 5
        email_textfield.setLeftPaddingPoints(20)
        password_textfield.setLeftPaddingPoints(20)
        Name_textfield.setLeftPaddingPoints(20)
        editprofile_btn.titleEdgeInsets.left = 20
        email_textfield.font = UIFont.init(name:"Museo", size: 20)
        password_textfield.font = UIFont.init(name:"Museo", size: 20)
        Name_textfield.font = UIFont.init(name:"Museo", size: 20)


        Name_textfield.attributedPlaceholder = NSAttributedString(string: "NAME",
                                                                  attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
        email_textfield.attributedPlaceholder = NSAttributedString(string: "EMAIL",
                                                                  attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
        
        password_textfield.attributedPlaceholder = NSAttributedString(string: "PASSWORD",
                                                                  attributes: [NSAttributedString.Key.foregroundColor: UIColor.white])
    }
     func updateprofile()
      {
        token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
       id = ((UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as? Int)!
     let header: HTTPHeaders = ["Authorization" : "Bearer \(token)"]
                    print(header)
    //      let Auth_header    = [
    //                 "language" : keydata]
    let parameters: Parameters = [ "name" : Name_textfield.text!, "password" : password_textfield.text!, "email" : email_textfield.text!,"user_id" :id]
        print(parameters)

         AF.request (BaseUrl.edit_profile.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: header).responseJSON
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
                                                  let msg = JSON["message"] as! String
                                     let alert = UIAlertController(title: "Dice", message: msg, preferredStyle: UIAlertController.Style.alert)
                                     alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                                   self.present(alert, animated: true)
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

                                  case .failure(let error):
                                    print(error)

                                    break

                                  }
                  }
      
           
               
           }
    
    
    @IBAction func camerabtn(_ sender: Any) {
        self.showAlert()

    }
    
    @IBAction func store_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                let vc = storyboard.instantiateViewController(withIdentifier: "StoreViewController") as! StoreViewController
                      vc.arrayData = arrayData

                                  vc.value = "1"
                                self.navigationController?.pushViewController(vc,
                                 animated: true)
    }
    
    @IBAction func update_profile(_ sender: Any)
    {
        updateprofile()
    }

    
    @IBAction func logout_btn(_ sender: Any)
    {
        UserDefaults.standard.set(false, forKey: "islogin")

        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                     let vc = storyboard.instantiateViewController(withIdentifier: "loginViewController") as! loginViewController
                                     self.navigationController?.pushViewController(vc,
                                      animated: true)
        
    }
    
    @IBAction func events_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                   let vc = storyboard.instantiateViewController(withIdentifier: "EventViewController") as! EventViewController
                        vc.arrayData = arrayData
                                       vc.value = "1"
                   self.navigationController?.pushViewController(vc,
                    animated: true)
    }
    @IBAction func rewards_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                          let vc = storyboard.instantiateViewController(withIdentifier: "RewardsViewController") as! RewardsViewController
               vc.arrayData = arrayData

                                              vc.value = "1"
                          self.navigationController?.pushViewController(vc,
                           animated: true)
    }
    @IBAction func home_btn(_ sender: Any) {
                let storyboard = UIStoryboard(name: "Main", bundle: nil)
let vc = storyboard.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
        vc.arrayData = arrayData

                                       vc.value = "1"
                           self.navigationController?.pushViewController(vc,
                            animated: true)
    }
    @IBAction func back_btn(_ sender: Any)
    {
        self.navigationController?.popViewController(animated: true)
    }
    private func showAlert() {

        let alert = UIAlertController(title: "Image Selection", message: "From where you want to pick this image?", preferredStyle: .actionSheet)
        alert.addAction(UIAlertAction(title: "Camera", style: .default, handler: {(action: UIAlertAction) in
            self.getImage(fromSourceType: .camera)
        }))
        alert.addAction(UIAlertAction(title: "Photo Album", style: .default, handler: {(action: UIAlertAction) in
            self.getImage(fromSourceType: .photoLibrary)
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .destructive, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }

    //get image from source type
    private func getImage(fromSourceType sourceType: UIImagePickerController.SourceType) {

        //Check is source type available
        if UIImagePickerController.isSourceTypeAvailable(sourceType) {

            let imagePickerController = UIImagePickerController()
            imagePickerController.delegate = self
            imagePickerController.sourceType = sourceType
            self.present(imagePickerController, animated: true, completion: nil)
        }
    }

    //MARK:- UIImagePickerViewDelegate.
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {

        self.dismiss(animated: true) { [weak self] in

            guard let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage else { return }
            //Setting image to your image view
            self?.user_image.image = image
            self!.imagupload()
        }
    }

    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
     func imagupload(){
    let api_url = "http://141.136.36.151/api/image_upload_ios"
       guard let url = URL(string: api_url) else {
           return
       }

       var urlRequest = URLRequest(url: url, cachePolicy: .reloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10.0 * 1000)
       urlRequest.httpMethod = "POST"
       urlRequest.addValue("application/json", forHTTPHeaderField: "Accept")

       //Set Your Parameter
       let parameterDict = NSMutableDictionary()
       parameterDict.setValue(id, forKey: "user_id")
         //  parameterDict.setValue(self.profile_image.image, forKey: "image")


       //Set Image Data
       let imgData = self.user_image.image!.jpegData(compressionQuality: 0.75)!

      // Now Execute
       AF.upload(multipartFormData: { multiPart in
           for (key, value) in parameterDict {
               if let temp = value as? String {
                   multiPart.append(temp.data(using: .utf8)!, withName: key as! String)
               }
               if let temp = value as? Int {
                   multiPart.append("\(temp)".data(using: .utf8)!, withName: key as! String)
               }
               if let temp = value as? NSArray {
                   temp.forEach({ element in
                       let keyObj = key as! String + "[]"
                       if let string = element as? String {
                           multiPart.append(string.data(using: .utf8)!, withName: keyObj)
                       } else
                           if let num = element as? Int {
                               let value = "\(num)"
                               multiPart.append(value.data(using: .utf8)!, withName: keyObj)
                       }
                   })
               }
           }
           multiPart.append(imgData, withName: "image", fileName: "file.jpg", mimeType: "image/jpeg")
       }, with: urlRequest)
           .uploadProgress(queue: .main, closure: { progress in
               //Current upload progress of file
               print("Upload Progress: \(progress.fractionCompleted)")
           })
           .responseJSON(completionHandler: { data in
                      switch data.result {
                      case .success(_):
                       do {
                       
                       let dictionary = try JSONSerialization.jsonObject(with: data.data!, options: .fragmentsAllowed) as! NSDictionary
                           print("Success!")
                           print(dictionary)
                      }
                      catch {
                         // catch error.
                       print("catch error")

                             }
                       break
                           
                      case .failure(_):
                       print("failure")
                       break
                       
                   }

           })
        }
     
   
}
