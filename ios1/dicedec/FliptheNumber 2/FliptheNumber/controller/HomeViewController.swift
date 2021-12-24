//
//  HomeViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 08/12/20.
//

import UIKit
import Alamofire

class HomeViewController: UIViewController {
    @IBOutlet weak var joker: UILabel!
    
    @IBOutlet weak var diamond_lbl: UILabel!
    @IBOutlet weak var dollar: UILabel!
    var arrayData = NSArray()
    var value = ""
    var token: String = ""
    var id: Int = 0
    var dataArray =  NSArray()

    @IBOutlet weak var userimage: UIImageView!

    override func viewDidLoad() {
        super.viewDidLoad()
//        print(arrayData.value(forKey: "total_joker"))
//        print(arrayData.value(forKey: "total_token"))
//        print(arrayData.value(forKey: "image"))
//                print(arrayData)
//        if value == "1"
//        {
//            let data = arrayData[0] as? NSDictionary
//
////              let imageURL:URL = URL(string: (((data?.value(forKey: "image") ?? 0 ) as? String)!))!
////                    let dataimage = NSData(contentsOf: imageURL)
////                    userimage.image = UIImage(data: dataimage! as Data)
//            id = (data?.value(forKey: "user_id") ?? 0 ) as! Int
//                    dollar.text! = ((data?.value(forKey: "total_token") ?? 0 )  as AnyObject).description
//                    print(dollar.text as Any)
//                    joker.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
//                    print(joker.text as Any)
//
//        }
//        else
//        {
//            print(UserDefaults.standard.value(forKey:"total_token") ?? 0)
//            token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
//            print(token)
//            id = (UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as! Int
//            joker.text = (UserDefaults.standard.value(forKey: "total_joker") ?? 0 ) as? String
//            dollar.text = (UserDefaults.standard.value(forKey: "total_token") ?? 0 ) as? String
////              let imageURL:URL = URL(string: (((UserDefaults.standard.value(forKey: "image") ?? 0 ) as? String)!))!
////                let dataimage = NSData(contentsOf: imageURL)
////                userimage.image = UIImage(data: dataimage! as Data)
//        }
        getdata()


    }
    func getdata()
      {
        token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
       id = ((UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as? Int)!
     let header: HTTPHeaders = ["Authorization" : "Bearer \(token)"]
                    print(header)
    //      let Auth_header    = [
    //                 "language" : keydata]
    let parameters: Parameters = [ "user_id" :id]
        print(parameters)

         AF.request (BaseUrl.GetHomePageDetails.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: header).responseJSON
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
                                                self.arrayData = responseInfo
                                                print(self.arrayData)
                                               // let data = self.arrayData[0] as? NSDictionary
                                                self.joker.text! = ((self.arrayData[0] as AnyObject) .value(forKey: "total_joker") as? String)!
                                                self.dollar.text! = ((self.arrayData[0] as AnyObject) .value(forKey: "total_token") as? String)!
                                                let imageURL:URL = URL(string: (((self.arrayData[0] as AnyObject).value(forKey: "image") ?? 0 ) as? String)!)!
                                                let dataimage = NSData(contentsOf: imageURL)
                                                
                                                self.userimage.image = UIImage(data: dataimage! as Data)
                                                 // self.userimage.myViewCorners()
                                                self.userimage.layer.cornerRadius =
                                                self.userimage.frame.width/2
                                                self.userimage.layer.masksToBounds = true
                                                print(self.joker.text!)

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
    
    override func viewWillAppear(_ animated: Bool) {
        //getdata()
    }
    @IBAction func tournament_btn(_ sender: Any)
    {
    }
    
    @IBAction func challenge_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                   let vc = storyboard.instantiateViewController(withIdentifier: "challengefriendViewController") as! challengefriendViewController
        
                   self.navigationController?.pushViewController(vc,
                    animated: true)
    }
    @IBAction func player_btn(_ sender: Any) {
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                         let vc = storyboard.instantiateViewController(withIdentifier: "playerViewController") as! playerViewController
              
                         self.navigationController?.pushViewController(vc,
                          animated: true)
    }
    
    @IBAction func computer_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                          let vc = storyboard.instantiateViewController(withIdentifier: "gameViewController") as! gameViewController
               
                          self.navigationController?.pushViewController(vc,
                           animated: true)
               
    }
    
    @IBAction func rewards_btn(_ sender: Any)
    {
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                   let vc = storyboard.instantiateViewController(withIdentifier: "RewardsViewController") as! RewardsViewController
        vc.arrayData = arrayData
        vc.value = "1"


                   self.navigationController?.pushViewController(vc,
                    animated: true)
    }
    @IBAction func home_btn(_ sender: Any) {
//        let storyboard = UIStoryboard(name: "Main", bundle: nil)
//                   let vc = storyboard.instantiateViewController(withIdentifier: "gameViewController") as! gameViewController
//
//                   self.navigationController?.pushViewController(vc,
//                    animated: true)
//
    }
    
    @IBAction func profile_btn(_ sender: Any) {
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
 let vc = storyboard.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
        vc.arrayData = arrayData
        print(arrayData)
        vc.value = "1"
        
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    @IBAction func events_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                 let vc = storyboard.instantiateViewController(withIdentifier: "EventViewController") as! EventViewController
                      vc.arrayData = arrayData
                        vc.value = "1"
                                 self.navigationController?.pushViewController(vc,
                                  animated: true)
    }
    
    
    @IBAction func store_btn(_ sender: Any) {
        
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                          let vc = storyboard.instantiateViewController(withIdentifier: "StoreViewController") as! StoreViewController
               vc.arrayData = arrayData
                vc.value = "1"
                          self.navigationController?.pushViewController(vc,
                           animated: true)
       
    }
   

}
extension UIView {
    //If you want only round corners
    func myViewCorners() {
        layer.cornerRadius = 20
      
        layer.masksToBounds = true
    }
    
    //If you want complete round shape, enable above comment line
    func myViewCorners(width:CGFloat) {
        layer.cornerRadius = width/2
        layer.masksToBounds = true
    }
}
