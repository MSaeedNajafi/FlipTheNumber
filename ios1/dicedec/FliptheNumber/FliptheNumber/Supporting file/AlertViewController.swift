//
//  AlertViewController.swift
//  RealWorld
//
//  Created by SAT PAL on 08/06/20.
//  Copyright © 2020 ArthTech Solutions. All rights reserved.
//

protocol DismissAlertDelegate : class {
    
    func tapNoButton()
    func tapYesButton()
}

import UIKit

class AlertViewController: UIViewController {

  
    @IBOutlet weak var alertContainer_view: UIView!
    
  
    @IBOutlet weak var message_label: UILabel!
    
    weak var delegate : DismissAlertDelegate?
    
    var msg = ""
    
    var yes = false
    
    @IBOutlet weak var yes_btn: UIButton!
    
    @IBOutlet weak var no_btn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        message_label.text = msg
        
        if yes {
            
            yes_btn.isHidden = false
            
            no_btn.setTitle("No", for: .normal)
            
        }else {
            
            yes_btn.isHidden = true
            no_btn.setTitle("OK", for: .normal)
        }
       
    }
    
    
    
    override func viewWillLayoutSubviews() {
        
        alertContainer_view.layer.cornerRadius = 10
    }
    

    @IBAction func tap_no_btn(_ sender: Any) {
        
        self.dismiss(animated: false) {
            
             [weak self] in
            
            self?.delegate?.tapNoButton()
        }
        
      
    }
    
   
    
    
    @IBAction func tap_yes_btn(_ sender: Any) {
        
        self.dismiss(animated: false) {
                   
                    [weak self] in
                   
                   self?.delegate?.tapYesButton()
               }
               
    }
    
   
    
}
