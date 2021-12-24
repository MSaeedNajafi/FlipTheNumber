//
//  RewardsViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 08/12/20.
//

import UIKit

class RewardsViewController: UIViewController {
    var token: String = ""
       var id: Int = 0
    var arrayData = NSArray()
      var value = ""
    @IBOutlet weak var dollar_lbl: UILabel!
    @IBOutlet weak var diamond_lbl: UILabel!
    @IBOutlet weak var joker_lbl: UILabel!
    @IBOutlet weak var dollar_btn: UIButton!
    @IBOutlet weak var crystal_btn: UIButton!
    override func viewDidLoad() {
        super.viewDidLoad()
        dollar_btn.layer.cornerRadius = 10
        crystal_btn.layer.cornerRadius = 10
        if value == "1"
               {
                let data = arrayData[0] as? NSDictionary

                   id = (data?.value(forKey: "user_id") ?? 0 ) as! Int
                           dollar_lbl.text! = ((data?.value(forKey: "total_token") ?? 0 )  as AnyObject).description
                           print(dollar_lbl.text as Any)
                           joker_lbl.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
                           print(joker_lbl.text as Any)

               }
        else
        {
        token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
        print(token)
        id = (UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as! Int
        joker_lbl.text = (UserDefaults.standard.value(forKey: "total_joker") ?? 0 ) as? String
        dollar_lbl.text = (UserDefaults.standard.value(forKey: "total_token") ?? 0 ) as? String
        }
        // Do any additional setup after loading the view.
    }
    
    @IBAction func store_btn(_ sender: Any)
    {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                let vc = storyboard.instantiateViewController(withIdentifier: "StoreViewController") as! StoreViewController
                     vc.arrayData = arrayData
                          vc.value = "1"
                                self.navigationController?.pushViewController(vc,
                                 animated: true)
    }
    @IBAction func event_btn(_ sender: Any)
    {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                                        let vc = storyboard.instantiateViewController(withIdentifier: "EventViewController") as! EventViewController
                             vc.arrayData = arrayData
                                  vc.value = "1"
                                        self.navigationController?.pushViewController(vc,
                                         animated: true)
    }
    @IBAction func profile_btn(_ sender: Any)
    {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                               let vc = storyboard.instantiateViewController(withIdentifier: "ProfileViewController") as! ProfileViewController
                    vc.arrayData = arrayData
                         vc.value = "1"
                               self.navigationController?.pushViewController(vc,
                                animated: true)
    }
    @IBAction func home_btn(_ sender: Any)
    {
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
    @IBAction func crystal_btn(_ sender: Any)
    {
    }
    @IBAction func dollar_btn(_ sender: Any)
    {
          
    }
  

}
