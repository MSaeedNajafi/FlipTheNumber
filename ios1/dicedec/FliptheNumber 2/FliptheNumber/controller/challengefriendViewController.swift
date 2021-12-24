//
//  challengefriendViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 10/12/20.
//

import UIKit
import Alamofire

class challengefriendViewController: BaseViewController,UITableViewDataSource,UITableViewDelegate {
   var token: String = ""
    var id: Int = 0
    var dataArray =  NSArray()
    var friendid: Int = 0
    var acceptfriendid: Int = 0

     var val = ""
    var objCell = FriendCell()


    @IBOutlet weak var nodate_lbl: UILabel!
    @IBOutlet weak var table_list: UITableView!
    @IBOutlet weak var invite_lbl: UILabel!
    @IBOutlet weak var alluser_lbl: UILabel!
    @IBOutlet weak var request_lbl: UILabel!
    @IBOutlet weak var friend_lbl: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        invite_lbl.isHidden = true
        request_lbl.isHidden = true
        friend_lbl.isHidden = true
        self.objCell.cancel_btn?.isHidden = true
        self.objCell.accept_btn?.isHidden = true

        nodate_lbl.isHidden = true
        table_list.dataSource = self
        table_list.delegate = self
        getuser()
        // Do any additional setup after loading the view.
    }
    func getuser()
          {
            token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
           id = ((UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as? Int)!
         let header: HTTPHeaders = ["Authorization" : "Bearer \(token)"]
                        print(header)
        //      let Auth_header    = [
        //                 "language" : keydata]
        let parameters: Parameters = [ "user_id" :id]
            print(parameters)

             AF.request (BaseUrl.user_list.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: header).responseJSON
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
                                            
                                            if responseInfo.count == 0
                                            {
                                                self.nodate_lbl.isHidden = false
                                            }

                                            if status == true
                                            {
                                                self.dataArray = responseInfo
                                                DispatchQueue.main.async {
                                                   UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "user_id"), forKey: "friend_id")
                                                    self.val = "1"

                                                    self.table_list.reloadData()
 
                                                }
                                            }
                                            else
                                            {
                                                
                                           //     se\lf.nodate_lbl.isHidden = true

//                                                let msg = JSON["message"] as! String
//                                                let alert = UIAlertController(title: "Dice", message: msg, preferredStyle: UIAlertController.Style.alert)
//                                                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
//                                              self.present(alert, animated: true)

                                                ActivityIndicator.shared.hideLoadingIndicator()

                                            }
                                                           }

                                      case .failure(let error):
                                        print(error)

                                        break

                                      }
                      }
          
               
                   
               }
    func sendrequest()
              {
                val = "1"
                friendid = ((UserDefaults.standard.value(forKey: "friend_id") ?? 0 ) as? Int)!
                token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
               id = ((UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as? Int)!
             let header: HTTPHeaders = ["Authorization" : "Bearer \(token)"]
                            print(header)
            //      let Auth_header    = [
            //                 "language" : keydata]
                let parameters: Parameters = [ "user_id" :id,"frnd_id" :friendid]
                print(parameters)

                 AF.request (BaseUrl.sendrequest.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: header).responseJSON
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
                                                    
                                                    if self.dataArray.count == 0
                                                    {
                                                        
                                                        
                                                    }
                                                    else
                                                    {
                                                        self.dataArray = responseInfo
                                                                                                                    DispatchQueue.main.async {
                                                                                                                        self.table_list.reloadData()
                                                    
                                                    
                                                    
                                                    self.dataArray = responseInfo
                                                    DispatchQueue.main.async {
                                                        let msg = JSON["message"] as! String
                                                        UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "user_id"), forKey: "friend_id")
                                                                                                           let alert = UIAlertController(title: "Dice", message: msg, preferredStyle: UIAlertController.Style.alert)
                                                                                                           alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                                                                                                         self.present(alert, animated: true)
                                                    }
                                                }
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
        
    @IBAction func user_btn(_ sender: Any) {
        alluser_lbl.isHidden = false
        invite_lbl.isHidden = true
        request_lbl.isHidden = true
        friend_lbl.isHidden = true
        print((sender as AnyObject).tag as Any)
        

        
          val = "1"
        getuser()

    }
    @IBAction func friend_btn(_ sender: Any) {
    }
    
    
    @IBAction func back_btn(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    @IBAction func invite_btn(_ sender: Any) {
        request_lbl.isHidden = true
             invite_lbl.isHidden = false
             friend_lbl.isHidden = true
             alluser_lbl.isHidden = true


                         token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
                        id = ((UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as? Int)!
                      let header: HTTPHeaders = ["Authorization" : "Bearer \(token)"]
                                     print(header)
                     //      let Auth_header    = [
                     //                 "language" : keydata]
                         let parameters: Parameters = [ "user_id" :id]
                         print(parameters)

                          AF.request (BaseUrl.invite.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: header).responseJSON
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
                                                         if responseInfo.count == 0
                                                         {
                                                             self.nodate_lbl.isHidden = false
                                                         }

                                                         if status == true
                                                         {
                                                            if self.dataArray.count == 0
                                                            {
                                                                
                                                                
                                                            }
                                                            else
                                                            {
                                                                self.dataArray = responseInfo
                                                                                                                            DispatchQueue.main.async {
                                                                                                                                self.table_list.reloadData()
                                                            }

                                                            
                                                            
              
                                                             }
                                                         }
                                                         else
                                                         {
                                                             
                                                        //     self.nodate_lbl.isHidden = true

             //                                                let msg = JSON["message"] as! String
             //                                                let alert = UIAlertController(title: "Dice", message: msg, preferredStyle: UIAlertController.Style.alert)
             //                                                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
             //                                              self.present(alert, animated: true)

                                                             ActivityIndicator.shared.hideLoadingIndicator()

                                                         }
                                                                        }

                                                   case .failure(let error):
                                                     print(error)

                                                     break

                                                   }
                                   }
                       
                            
    }
    @objc func sendrequest_btn(sender : UIButton) {
        val = "1"
        sendrequest()

    }
    @objc func cancelrequest_btn(sender : UIButton) {
//             print(sender.tag)
//
//
//             let buttonPosition = sender.convert(CGPoint.zero, to: self.table_list)
//             let IndexPath = self.table_list.indexPathForRow(at: buttonPosition)
//
//             let cell = self.table_list.cellForRow(at: IndexPath!) as! FriendCell
//             print(cell)
//

    }
     @objc func accept_requestbtn(sender : UIButton)
     {

                          token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
                         acceptfriendid = ((UserDefaults.standard.value(forKey: "id") ?? 0 ) as? Int)!
                       let header: HTTPHeaders = ["Authorization" : "Bearer \(token)"]
                                      print(header)
                      //      let Auth_header    = [
                      //                 "language" : keydata]
                          let parameters: Parameters = [ "id" :acceptfriendid]
                          print(parameters)

                           AF.request (BaseUrl.accept_request.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: header).responseJSON
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
                                                              self.dataArray = responseInfo
                                                              DispatchQueue.main.async {
                                                                                                                let msg = JSON["message"] as! String
                                                                                                                let alert = UIAlertController(title: "Dice", message: msg, preferredStyle: UIAlertController.Style.alert)
                                                                                                                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
                                                                                                              self.present(alert, animated: true)
                                                                
                                                                  self.table_list.reloadData()
               
                                                              }
                                                          }
                                                          else
                                                          {
                                                              
                                                         //     self.nodate_lbl.isHidden = true

              //                                                let msg = JSON["message"] as! String
              //                                                let alert = UIAlertController(title: "Dice", message: msg, preferredStyle: UIAlertController.Style.alert)
              //                                                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
              //                                              self.present(alert, animated: true)

                                                              ActivityIndicator.shared.hideLoadingIndicator()

                                                          }
                                                                         }

                                                    case .failure(let error):
                                                      print(error)

                                                      break

                                                    }
                                    }
                             
    

        }
    
    @IBAction func request_btn(_ sender: Any)
    {
        request_lbl.isHidden = false
        invite_lbl.isHidden = true
        friend_lbl.isHidden = true
        alluser_lbl.isHidden = true
        val = "3"


                    token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
                   id = ((UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as? Int)!
                 let header: HTTPHeaders = ["Authorization" : "Bearer \(token)"]
                                print(header)
                //      let Auth_header    = [
                //                 "language" : keydata]
                    let parameters: Parameters = [ "user_id" :id]
                    print(parameters)

                     AF.request (BaseUrl.myrequest.toURL(), method: .post, parameters: parameters,encoding: JSONEncoding.default, headers: header).responseJSON
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
                                                    
                                                    if responseInfo.count == 0
                                                    {
                                                     //   self.nodate_lbl.isHidden = false
                                                    }

                                                    if status == true
                                                    {
                                                        
                                                        if  responseInfo.count == 0
                                                        {
                                                            self.table_list.reloadData()

                                                        }
                                                        else
                                                        {
                                                        self.dataArray = responseInfo
                                                        DispatchQueue.main.async {
                                                            UserDefaults.standard.setValue((responseInfo[0] as AnyObject).value(forKey: "id"), forKey: "id")
                                                            self.table_list.reloadData()
         
                                                        }
                                                    }
                                                    }
                                                    else
                                                    {
                                                        
                                                   //     self.nodate_lbl.isHidden = true

        //                                                let msg = JSON["message"] as! String
        //                                                let alert = UIAlertController(title: "Dice", message: msg, preferredStyle: UIAlertController.Style.alert)
        //                                                alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
        //                                              self.present(alert, animated: true)

                                                        ActivityIndicator.shared.hideLoadingIndicator()

                                                    }
                                                                   }

                                              case .failure(let error):
                                                print(error)

                                                break

                                              }
                              }
                       
                           
                       }
   
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataArray.count

       }
            
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
       {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! FriendCell
        objCell = cell
        cell.contentView.backgroundColor = UIColor.clear
        cell.backgroundColor = UIColor.clear
        cell.name_lbl .text = (self.dataArray[indexPath.row] as AnyObject) .value(forKey: "name") as? String
        cell.email_lbl .text = (self.dataArray[indexPath.row] as AnyObject) .value(forKey: "email") as? String
        cell.request_btn.layer.cornerRadius = 5
        cell.cancel_btn.layer.cornerRadius = 5
        cell.accept_btn.layer.cornerRadius = 5

        cell.round_view.layer.cornerRadius = 5
        cell.request_btn.tag = indexPath.row
        cell.cancel_btn.tag = indexPath.row
        cell.accept_btn.tag = indexPath.row

         if val == "3"
         {
            cell.request_btn.isHidden = true
            cell.cancel_btn.isHidden = false
            cell.accept_btn.isHidden = false
            cell.cancel_btn.addTarget(self, action: #selector(cancelrequest_btn), for: .touchUpInside)
              cell.accept_btn.addTarget(self, action: #selector(accept_requestbtn), for: .touchUpInside)

        }
        else  if val == "1"
         {
            cell.cancel_btn.isHidden = true
            cell.accept_btn.isHidden = true

            cell.request_btn.isHidden = false

        cell.request_btn.addTarget(self, action: #selector(sendrequest_btn), for: .touchUpInside)
        }
        
              return cell
       }
       
}
