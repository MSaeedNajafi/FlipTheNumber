//
//  BaseViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 15/09/20.
//

import UIKit

class BaseViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    func showAlertWithouTitle(message: String) {
              let alert = UIAlertController(title: "Dice", message: message, preferredStyle: UIAlertController.Style.alert)
              alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.destructive, handler: nil))
              self.present(alert, animated: true, completion: nil)
          }
    
    func showAlert(_ title: String = "DICE", message: String) {
           let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertController.Style.alert)
           alert.addAction(UIAlertAction(title: "Ok", style: UIAlertAction.Style.destructive, handler: nil))
           self.present(alert, animated: true, completion: nil)
       }
    
    
        func showAlertDismiss(Title: String , Message: String , ButtonTitle: String) {

                  let alert = UIAlertController(title: Title, message: Message, preferredStyle: UIAlertController.Style.alert)

                  alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { _ in

                      self.dismiss(animated: true, completion: nil)

                  }))

                  self.present(alert, animated: true, completion: nil)

              }
    
       func showAlertAction(Title: String , Message: String , ButtonTitle: String) {

           let alert = UIAlertController(title: Title, message: Message, preferredStyle: UIAlertController.Style.alert)

           alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { _ in

               self.navigationController?.popViewController(animated: true)

           }))

           self.present(alert, animated: true, completion: nil)

       }
       
       func showAlertAction_pushType(Title: String , Message: String , ButtonTitle: String, vc: UIViewController?) {
           
           let alert = UIAlertController(title: Title, message: Message, preferredStyle: UIAlertController.Style.alert)
           
           alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: { _ in
               
               self.navigationController?.pushViewController(vc!, animated: true)
               
               
           }))
           
           self.present(alert, animated: true, completion: nil)
           
       }
    
}
