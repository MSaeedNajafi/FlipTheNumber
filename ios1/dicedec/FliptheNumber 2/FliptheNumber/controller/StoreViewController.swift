//
//  StoreViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 08/12/20.
//

import UIKit
import Alamofire
class StoreViewController: BaseViewController,UITableViewDelegate,UITableViewDataSource {
    var token: String = ""
          var id: Int = 0
    var arrayData = NSArray()
       var value = ""
    var dataArray =  NSArray()

    @IBOutlet weak var nodata_lbl: UILabel!
    @IBOutlet weak var diamond_lbl: UILabel!
    @IBOutlet weak var dollar_lbl: UILabel!
    @IBOutlet weak var joker_lbl: UILabel!
    @IBOutlet weak var tableview_data: UITableView!
    override func viewDidLoad()
    {
        super.viewDidLoad()

        tableview_data.delegate = self
        tableview_data.dataSource = self
        print(arrayData)
        nodata_lbl.isHidden = true
        
        if value == "1"
        {
          let data = arrayData[0] as? NSDictionary

                //dollar.text! = ((arrayData[0] as AnyObject).value(forKey: "total_token") as? String)!
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
        getstore()

        // Do any additional setup after loading the view.
    }
    func getstore()
    {
        ActivityIndicator.shared.showLoadingIndicator(text: "Loading..")

    AF.request("http://141.136.36.151/api/stores", method: .get)
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
                                self.tableview_data.reloadData()
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
    
    @IBAction func back_btn(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return dataArray.count
          }
    
    
   func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell
   {
           
    let cell = tableView.dequeueReusableCell(withIdentifier: "tablestore", for: indexPath) as! tablestore
    cell.contentView.backgroundColor = UIColor.clear
    cell.backgroundColor = UIColor.clear
    cell.store_view.layer.cornerRadius = 5
    cell.dice_lbl.text = (self.dataArray[indexPath.row] as AnyObject) .value(forKey: "name") as? String
     cell.dice_lbl.font = UIFont.init(name:"Museo", size: 16)
    cell.token_lbl.font = UIFont.init(name:"Museo", size: 16)
    cell.pricelbl.font = UIFont.init(name:"Museo", size: 14)


    cell.token_lbl.text = (self.dataArray[indexPath.row] as AnyObject) .value(forKey: "number") as? String
    cell.pricelbl.text = (self.dataArray[indexPath.row] as AnyObject) .value(forKey: "price") as? String
   if let urlString = (self.dataArray[indexPath.row] as AnyObject).value(forKey: "store_img") as? String,
    let url = URL(string: urlString) {
           URLSession.shared.dataTask(with: url) { (data, urlResponse, error) in
               if let data = data
               {
                
                cell.dice_image.image = UIImage(data: data)
               }
           }.resume()
       }
 
              return cell
          }
    
    
    @IBAction func event_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                        let vc = storyboard.instantiateViewController(withIdentifier: "EventViewController") as! EventViewController
                             vc.arrayData = arrayData
                                  vc.value = "1"
                                        self.navigationController?.pushViewController(vc,
                                         animated: true)
    }
    @IBAction func revie_btn(_ sender: Any) {
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
    
    @IBAction func profile_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                let vc = storyboard.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
                     vc.arrayData = arrayData
                          vc.value = "1"
                                self.navigationController?.pushViewController(vc,
                                 animated: true)
    }
    
    

}
