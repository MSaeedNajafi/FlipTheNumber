//
//  ViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 15/09/20.
//

import UIKit
import IQKeyboardManagerSwift

class ViewController: BaseViewController {
    

    
    @IBOutlet weak var signup_btn: UIButton!
    @IBOutlet weak var login_btn: UIButton!
   
    override func viewDidLoad() {
        super.viewDidLoad()
        login_btn.layer.cornerRadius = 10
        signup_btn.layer.cornerRadius = 10
        login_btn.titleLabel?.font =  UIFont(name: "Museo", size: 18)
        signup_btn.titleLabel?.font =  UIFont(name: "Museo", size: 18)


   //     self.navigationController?.isNavigationBarHidden = true

       
        // Do any additional setup after loading the view.
    }

    @IBAction func login_btn(_ sender: Any)
    {
        //if checkValidation() {
            let storyboard = UIStoryboard(name: "Main", bundle: nil)
            let vc = storyboard.instantiateViewController(withIdentifier: "loginViewController") as! loginViewController
            self.navigationController?.pushViewController(vc,
             animated: true)
    //  }
        
    }
   
    @IBAction func signup_btn(_ sender: Any) {

        let storyboard = UIStoryboard(name: "Main", bundle: nil)
        let vc = storyboard.instantiateViewController(withIdentifier: "signupVc") as! signupVc
        self.navigationController?.pushViewController(vc,
         animated: true)
        
     
    }
    
    }

