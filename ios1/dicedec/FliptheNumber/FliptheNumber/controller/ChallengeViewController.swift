//
//  ChallengeViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 25/09/20.
//


import UIKit
class ChallengeViewController: BaseViewController {
    let randomBool = Bool.random()


    @IBOutlet weak var flipno: UIImageView!
    @IBOutlet weak var yellowdice: UIImageView!
    @IBOutlet weak var yellowdiceboard: UIImageView!
    @IBOutlet weak var reddice: UIImageView!
    @IBOutlet weak var reddiceboard: UIImageView!

    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.isNavigationBarHidden = true
        self.animationveiw()
print(randomBool)
        self.rotatanum()

        // Do any additional setup after loading the view.
    }
    func animationveiw()
    {
        UIView.animate(withDuration: 10.0, animations: {
           self.reddice.transform = self.reddice.transform.rotated(by: CGFloat(Double.pi / 2))
        })

        UIView.animate(withDuration: 10.0, animations: {
                  self.yellowdice.transform = self.reddice.transform.rotated(by: CGFloat(Double.pi / 2))
           
               })
        UIView.animate(withDuration: 1, animations: {
            
            self.yellowdiceboard.transform = CGAffineTransform(translationX: -self.yellowdice.frame.width , y: 0)
        
          })
            { _ in
              UIView.animate(withDuration: 1) {
                  self.yellowdiceboard.transform = CGAffineTransform.identity
              }
          }
            UIView.animate(withDuration: 1, animations: {
                  
                  self.reddiceboard.transform = CGAffineTransform(translationX: self.reddiceboard.frame.width , y:0)
              
                })
                  { _ in
                    UIView.animate(withDuration: 1) {
                        self.reddiceboard.transform = CGAffineTransform.identity
                    }
                }
    }
    func  rotatanum()
    {
       self.flipno.isHidden = true
        self.flipno.alpha = 0.0
        UIView.animate(withDuration: 2.0, delay: 1.0, options: UIView.AnimationOptions(rawValue: 0), animations: {
            self.flipno.alpha = 1.0
        }, completion: { finished in
            self.flipno.isHidden = false
            let vc = self.storyboard?.instantiateViewController(withIdentifier: "gameViewController") as! gameViewController
            //vc.num = self.randomBool
          //  print(vc.num)
                               self.navigationController?.pushViewController(vc,
                                                 animated: true)

        })   }
    
//    let vc = self.storyboard?.instantiateViewController(withIdentifier: "gameViewController") as! gameViewController
//                self.navigationController?.pushViewController(vc,
//                    animated: true)
    
    @IBAction func backbtn(_ sender: Any) {
      
    }
    
}
