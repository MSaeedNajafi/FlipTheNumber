//
//  playerViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 10/12/20.
//

import UIKit

class playerViewController: UIViewController {
    @IBOutlet weak var diceimage2: UIImageView!
    @IBOutlet weak var diceimage: UIImageView!
    @IBOutlet weak var lbl1_no: UILabel!
    @IBOutlet weak var lbl2_no: UILabel!
    @IBOutlet weak var lbl3_no: UILabel!
    @IBOutlet weak var lbl4_no: UILabel!
    @IBOutlet weak var lbl5_no: UILabel!
    @IBOutlet weak var lbl6_no: UILabel!
    @IBOutlet weak var lbl8_no: UILabel!
    @IBOutlet weak var lbl7_no: UILabel!
    @IBOutlet weak var lbl9: UILabel!
    @IBOutlet weak var turnlbl: UILabel!
    @IBOutlet weak var main_view: UIView!
    @IBOutlet weak var quit_view: UIView!
    @IBOutlet weak var winnerview: UIView!
    @IBOutlet weak var winner_lbl: UILabel!

    @IBOutlet weak var player2_btn: UIButton!
    @IBOutlet weak var player1_btn: UIButton!
    
    @IBOutlet weak var okay_btn: UIButton!
    
    var checkplaye = Int()
      var num =  Bool.random()
      var array = [String]()

    override func viewDidLoad() {
        super.viewDidLoad()
        main_view.isHidden = true
               quit_view.isHidden = true
        print(num)
          print_value(alreadyGreeted: num)

        // Do any additional setup after loading the view.
    }
    
    func print_value(alreadyGreeted: Bool) {
        if alreadyGreeted
        {
            turnlbl.text = "Player 1"
            player2_btn.isEnabled = false
            player1_btn.isEnabled = true


            let rotationAnimation = CABasicAnimation(keyPath: "transform.rotation")
                rotationAnimation.fromValue = 0.0
                rotationAnimation.toValue = Double.pi * 2
                rotationAnimation.duration = 1.0
            self.diceimage.layer.add(rotationAnimation, forKey: nil)
            let rotationAnimation1 = CABasicAnimation(keyPath: "transform.rotation")
            rotationAnimation1.fromValue = 0.0
              rotationAnimation1.toValue = Double.pi * 2
            rotationAnimation1.duration = 1.0
              self.diceimage2.layer.add(rotationAnimation, forKey: nil)
        }
        else
        {
            turnlbl.text = "Player 2"
            player1_btn.isEnabled = false
            player2_btn.isEnabled = true

            let rotationAnimation = CABasicAnimation(keyPath: "transform.rotation")
                rotationAnimation.fromValue = 0.0
                  rotationAnimation.toValue = Double.pi * 2
                rotationAnimation.duration = 1.0
                               self.diceimage.layer.add(rotationAnimation, forKey: nil)
            let rotationAnimation2 = CABasicAnimation(keyPath: "transform.rotation")
                rotationAnimation2.fromValue = 0.0
                rotationAnimation2.toValue = Double.pi * 2
                rotationAnimation2.duration = 1.0

            self.diceimage2.layer.add(rotationAnimation, forKey: nil)
//            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
//                self.diceclick(turnValue: alreadyGreeted)
//            }
        }
    }

    func uniq<S : Sequence, T : Hashable>(source: S) -> [T] where S.Iterator.Element == T {
        var buffer = [T]()
        var added = Set<T>()
        for elem in source {
            if !added.contains(elem) {
            buffer.append(elem)
            added.insert(elem)
            }
        }
        return buffer
    }
    @IBAction func backbtn(_ sender: Any) {
    //        let viewControllers: [UIViewController] = self.navigationController!.viewControllers ;
    //
    //            for aViewController in viewControllers {
    //                if(aViewController is HomeViewController){
    //                     self.navigationController!.popToViewController(aViewController, animated: true);
    //                }
    //            }
            
            main_view.isHidden = false
            quit_view.isHidden = false
            winnerview.isHidden = true
        }
    
    @IBAction func okay_btn(_ sender: Any) {
        main_view.isHidden = true
                   winnerview.isHidden = true
        self.navigationController?.popViewController(animated: true)
     
    }
    @IBAction func player2btn(_ sender: Any)
    {
        diceclick(turnValue: false)
    }
    
      @IBAction func dice_btn(_ sender: Any) {
          // diceResult()
        diceclick(turnValue: true)

      //  print_value(alreadyGreeted: num)

         
      }
      //false 0
      func diceclick(turnValue: Bool)
      {
          var uniqueVals = [String]()
          let numberOne = arc4random_uniform(6) + 1

                  let numberTwo = arc4random_uniform(6) + 1


                let i = Int(numberOne) + Int(numberTwo)
                 print(i)
                 if (numberOne == 1 || numberTwo == 1 || i == 1 )
                       {

                        diceimage.image = UIImage(named: "Dice\(numberOne)")

                        diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                         lbl1_no.textColor =  UIColor.red
                          array.append("1")
                          
          }

                        
                 if (numberOne == 2 || numberTwo == 2 || i == 2 )
                 {

                  diceimage.image = UIImage(named: "Dice\(numberOne)")

                  diceimage2.image = UIImage(named: "Dice\(numberTwo)")

                     lbl2_no.textColor =  UIColor.red
                   array.append("2")
                  
                 }
                 if (numberOne == 3 || numberTwo == 3 || i == 3 )
                        {

                         diceimage.image = UIImage(named: "Dice\(numberOne)")

                         diceimage2.image = UIImage(named: "Dice\(numberTwo)")

                            lbl3_no.textColor =  UIColor.red
                           array.append("3")
                 }
                  if (numberOne == 4 || numberTwo == 4 || i == 4 )
                        {

                         diceimage.image = UIImage(named: "Dice\(numberOne)")

                         diceimage2.image = UIImage(named: "Dice\(numberTwo)")

                            lbl4_no.textColor =  UIColor.red
                           array.append("4")
                 }
                 if (numberOne == 5 || numberTwo == 5 || i == 5 )
                            {

                             diceimage.image = UIImage(named: "Dice\(numberOne)")

                             diceimage2.image = UIImage(named: "Dice\(numberTwo)")

                                lbl5_no.textColor =  UIColor.red
                               array.append("5")
                     }
                 if (numberOne == 6 || numberTwo == 6 || i == 6 )
                                   {

                                    diceimage.image = UIImage(named: "Dice\(numberOne)")

                                    diceimage2.image = UIImage(named: "Dice\(numberTwo)")

                                       lbl6_no.textColor =  UIColor.red
                                       array.append("6")
                            }
                 print(i)
                 if (i == 7 )
                        {

                         diceimage.image = UIImage(named: "Dice\(numberOne)")

                         diceimage2.image = UIImage(named: "Dice\(numberTwo)")

                            lbl7_no.textColor =  UIColor.red
                           array.append("7")
                 }
                 if (i == 8 )
                              {

                               diceimage.image = UIImage(named: "Dice\(numberOne)")

                               diceimage2.image = UIImage(named: "Dice\(numberTwo)")

                                  lbl8_no.textColor =  UIColor.red
                                   array.append("8")
                       }
                 if (i == 9 )
                              {

                               diceimage.image = UIImage(named: "Dice\(numberOne)")

                               diceimage2.image = UIImage(named: "Dice\(numberTwo)")

                                  lbl9.textColor =  UIColor.red
                                   array.append("9")
                       }
          
          if array.count > 0 {
              uniqueVals = uniq(source: array)
          }
          //user win msg "You are winner"
        //  loss "Better luck next time"
          
          if(uniqueVals.count == 9) {
              if turnValue == true
                  {
                      
                      main_view.isHidden = false
                      
                      quit_view.isHidden = true
                      winnerview.isHidden = false
                      winner_lbl.text = "WINNER!"
              }
              else
              {
                  main_view.isHidden = false
                quit_view.isHidden = true
              winnerview.isHidden = false
               winner_lbl.text = "Better luck next time!"
              }
          }
          else {
                  if turnValue {
                      print_value(alreadyGreeted: false)
                  } else {
                      print_value(alreadyGreeted: true)
                  }
          }
          
      }
    @IBAction func yes_btn(_ sender: Any)
    {
           let storyboard = UIStoryboard(name: "Main", bundle: nil)
                     let vc = storyboard.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
                             self.navigationController?.pushViewController(vc,
                      animated: true)
       }
       @IBAction func no_btn(_ sender: Any)
       {
           quit_view.isHidden = true
           main_view.isHidden = true

       }
       @IBAction func home_btn(_ sender: Any) {
           let viewControllers: [UIViewController] = self.navigationController!.viewControllers ;

                      for aViewController in viewControllers {
                          if(aViewController is HomeViewController){
                               self.navigationController!.popToViewController(aViewController, animated: true);
                          }
                      }
       }

    
}
