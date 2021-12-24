//
//  EventViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 09/12/20.
//

import UIKit
import Alamofire

class EventViewController: UIViewController,UICollectionViewDataSource,UICollectionViewDelegate {
 
    
    @IBOutlet weak var nodata_lbl: UILabel!
    var token: String = ""
    @IBOutlet weak var joker_lbl: UILabel!
    var value = ""
    var arrayData = NSArray()
    var dataArray =  NSArray()

    @IBOutlet weak var collection_view: UICollectionView!
    @IBOutlet weak var diamond_lbl: UILabel!
    @IBOutlet weak var dollar_lbl: UILabel!
    var id: Int = 0
    override func viewDidLoad() {
        super.viewDidLoad()
        nodata_lbl.isHidden = true
        if value == "1"
              {
                  let data = arrayData[0] as? NSDictionary

//                    let imageURL:URL = URL(string: (((data?.value(forKey: "image") ?? 0 ) as? String)!))!
//                          let dataimage = NSData(contentsOf: imageURL)
//                          userimage.image = UIImage(data: dataimage! as Data)
                  id = (data?.value(forKey: "user_id") ?? 0 ) as! Int
                          dollar_lbl.text! = ((data?.value(forKey: "total_token") ?? 0 )  as AnyObject).description
                          print(dollar_lbl.text as Any)
                          joker_lbl.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
                          print(joker_lbl.text as Any)

              }
        else
        {
        print(UserDefaults.standard.value(forKey:"total_token") ?? 0)
              token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
              print(token)
              id = (UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as! Int
              joker_lbl.text = (UserDefaults.standard.value(forKey: "total_joker") ?? 0 ) as? String
              dollar_lbl.text = (UserDefaults.standard.value(forKey: "total_token") ?? 0 ) as? String
        }
        
        getevent()
    }
    
    @IBAction func store_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                       let vc = storyboard.instantiateViewController(withIdentifier: "StoreViewController") as! StoreViewController
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
    @IBAction func profile_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                   let vc = storyboard.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
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
    @IBAction func back_btn(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
 func getevent()
  {
      ActivityIndicator.shared.showLoadingIndicator(text: "Loading..")

  AF.request("http://141.136.36.151/api/events", method: .get)
      .responseJSON { (response) in

          switch response.result {
          case .success(let value):
              print(response.value!)

              if let JSON = value as? [String: Any]
              {
                  let status = JSON["status"] as! Bool
                  print(status)
                  let responseInfo = JSON["data"] as! NSArray
                if responseInfo.count == 0
                {
                    self.nodata_lbl.isHidden = false

                }

                  print(responseInfo)
                  if status == true
                  {
                      DispatchQueue.main.async
                          {
                              
                              self.dataArray = responseInfo
                              print(self.dataArray.value(forKey: "name"))
                            self.collection_view.reloadData()
                              ActivityIndicator.shared.hideLoadingIndicator()
                      }

                  }

                  
              }



          case .failure(let error):
              print(error)

              break

          }
  }
  }
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
       return dataArray.count
        //return 5

     }
     
     func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
         let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "eventcell", for: indexPath) as! eventcell
        cell.event_name.font = UIFont.init(name:"Museo", size: 11)

        cell.event_name.text = (self.dataArray[indexPath.row] as AnyObject) .value(forKey: "name") as? String
//        cell.event_name.text = (self.dataArray[indexPath.row] as AnyObject) .value(forKey: "name") as? String
        return cell

     }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize
{
   return CGSize(width: 300, height: 300)
}
    
    
}
