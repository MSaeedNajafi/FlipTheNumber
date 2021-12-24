//
//  gameViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 26/09/20.
//

import UIKit
import AVFoundation


class gameViewController: BaseViewController {
    @IBOutlet weak var coin_lbl: UILabel!
    let num = Bool.random()
    var checkplaye = Int()
    var array = [String]()
    var arrayDiceOne = [String]()
    var arrayDiceSecond = [String]()
    var arrayData = NSArray()
       var value = ""
       var token: String = ""
       var id: Int = 0
    @IBOutlet weak var winnerview: UIView!
    @IBOutlet weak var quit_view: UIView!
    @IBOutlet weak var main_view: UIView!
    @IBOutlet weak var winner_lbl: UILabel!
    @IBOutlet weak var win_bgview: UIView!
  
    @IBOutlet weak var dice_btn: UIButton!
    @IBOutlet weak var crystal_lbl: UILabel!
    @IBOutlet weak var turnlbl: UILabel!
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
    @IBOutlet weak var home_btn: UIButton!
    var objPlayer: AVAudioPlayer?
    @IBOutlet weak var user_image: UIImageView!
    @IBOutlet weak var user_name: UILabel!
    @IBOutlet weak var diamond_lbl1: UILabel!
    @IBOutlet weak var dollar_lbl: UILabel!
    @IBOutlet weak var diamond_lbl: UILabel!
    @IBOutlet weak var joker_lbl: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
//      if value == "1"
//              {
//                  let data = arrayData[0] as? NSDictionary
//
//                  
//                  id = (data?.value(forKey: "user_id") ?? 0 ) as! Int
//
//                          dollar_lbl.text! = ((arrayData[0] as AnyObject).value(forKey: "total_token") as? String)!
//               let imageURL:URL = URL(string: (((data?.value(forKey: "image") ?? 0 ) as? String)!))!
//                                 let dataimage = NSData(contentsOf: imageURL)
//                                 user_image.image = UIImage(data: dataimage! as Data)
//                  user_image!.layer.cornerRadius = 30
//                  user_image!.layer.masksToBounds = false
//                  user_image!.clipsToBounds = true
//                  user_image!.contentMode = UIView.ContentMode.scaleAspectFill
//      //               user_image.layer.cornerRadius = user_image.frame.wid/2 //This will change with corners of image and height/2 will make this circle shape
//      //               user_image.clipsToBounds = true
//                          
//                          dollar_lbl.text! = ((data?.value(forKey: "total_token") ?? 0 )  as AnyObject).description
//                          joker_lbl.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
//                  user_name.text! = ((data?.value(forKey: "name") ?? 0 )  as AnyObject).description
//
//                  joker_lbl.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
//              }
//              else
//              {
//
//              print(UserDefaults.standard.value(forKey:"total_token") ?? 0)
//              token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
//              print(token)
//              id = (UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as! Int
//              joker_lbl.text = (UserDefaults.standard.value(forKey: "total_joker") ?? 0 ) as? String
//              dollar_lbl.text = (UserDefaults.standard.value(forKey: "total_token") ?? 0 ) as? String
//                  user_name.text = (UserDefaults.standard.value(forKey: "name") ?? 0 ) as? String
//                  let imageURL:URL = URL(string: (((UserDefaults.standard.value(forKey: "image") ?? 0 ) as? String)!))!
//                      let dataimage = NSData(contentsOf: imageURL)
//                      user_image.image = UIImage(data: dataimage! as Data)
//                     user_image!.layer.cornerRadius = 30
//                           user_image!.layer.masksToBounds = false
//                           user_image!.clipsToBounds = true
//                           user_image!.contentMode = UIView.ContentMode.scaleAspectFill
//                                  
//              }
               
        print(num)
        self.navigationController?.isNavigationBarHidden = true
        print_value(alreadyGreeted: num)
        turnlbl.layer.cornerRadius = 5
        turnlbl.layer.masksToBounds = true
        main_view.isHidden = true
        quit_view.isHidden = true
       // main_view.addSubview(home_btn)
    }
    func playAudioFile() {
        guard let url = Bundle.main.url(forResource: "shake_dice", withExtension: "mp3") else { return }

        do {
            try AVAudioSession.sharedInstance().setCategory(AVAudioSession.Category.playback)
            try AVAudioSession.sharedInstance().setActive(true)

            // For iOS 11
            objPlayer = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)

            // For iOS versions < 11
            objPlayer = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)

            guard let aPlayer = objPlayer else { return }
            aPlayer.play()

        } catch let error {
            print(error.localizedDescription)
        }
    }
    
    func print_value(alreadyGreeted: Bool) {
        if alreadyGreeted {
            turnlbl.text = "Your Turn"
            dice_btn.isEnabled =  true
        }
        else
        {

            turnlbl.text = "Computer Turn"
            dice_btn.isEnabled =  false

            DispatchQueue.main.asyncAfter(deadline: .now() + 4) {
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
                self.playAudioFile()

                self.diceclick1(turnValue: alreadyGreeted)
            }
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
    @IBAction func dice_btn(_ sender: Any) {
        playAudioFile()
        
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
        
        diceclick1(turnValue: true)
       
    }
    //false 0
//    func diceclick(turnValue: Bool)
//    {
//        var uniqueVals = [String]()
//        let numberOne = arc4random_uniform(6) + 1
//                let numberTwo = arc4random_uniform(6) + 1
//              let i = Int(numberOne) + Int(numberTwo)
//               print(i)
//               if (numberOne == 1 || numberTwo == 1 || i == 1 )
//                     {
//
//                      diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                      diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
////                        if lbl1_no.textColor == UIColor.red || lbl1_no.textColor == UIColor.yellow
////                        {
////
////                        }
////                        else
////                        {
////
////                                               lbl1_no.textColor =  UIColor.red
////                                                array.append("1")
////
////
////
////
////                        }
//
//
//        }
//
//               if (numberOne == 2 || numberTwo == 2 || i == 2 )
//               {
//
//                diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
//                   lbl2_no.textColor =  UIColor.red
//                 array.append("2")
//
//               }
//               if (numberOne == 3 || numberTwo == 3 || i == 3 )
//                      {
//
//                       diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                       diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
//                          lbl3_no.textColor =  UIColor.red
//                         array.append("3")
//               }
//                if (numberOne == 4 || numberTwo == 4 || i == 4 )
//                      {
//
//                       diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                       diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
//                          lbl4_no.textColor =  UIColor.red
//                         array.append("4")
//               }
//               if (numberOne == 5 || numberTwo == 5 || i == 5 )
//                          {
//
//                           diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                           diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
//                              lbl5_no.textColor =  UIColor.red
//                             array.append("5")
//                   }
//               if (numberOne == 6 || numberTwo == 6 || i == 6 )
//                                 {
//
//                                  diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                                  diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
//                                     lbl6_no.textColor =  UIColor.red
//                                     array.append("6")
//                          }
//               print(i)
//               if (i == 7 )
//                      {
//
//                       diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                       diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
//                          lbl7_no.textColor =  UIColor.red
//                         array.append("7")
//               }
//               if (i == 8 )
//                            {
//
//                             diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                             diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
//                                lbl8_no.textColor =  UIColor.red
//                                 array.append("8")
//                     }
//               if (i == 9 )
//                            {
//
//                             diceimage.image = UIImage(named: "Dice\(numberOne)")
//
//                             diceimage2.image = UIImage(named: "Dice\(numberTwo)")
//
//
//                                lbl9.textColor =  UIColor.red
//                                 array.append("9")
//                     }
//
//        if array.count > 0 {
//            uniqueVals = uniq(source: array)
//        }
//        //user win msg "You are winner"
//      //  loss "Better luck next time"
//
//        if(uniqueVals.count == 9) {
//            if turnValue == true
//                {
//
//                    main_view.isHidden = false
//
//                    quit_view.isHidden = true
//                    winnerview.isHidden = false
//                    winner_lbl.text = "WINNER!"
//
//            }
//            else
//            {
//                main_view.isHidden = false
//              quit_view.isHidden = true
//            winnerview.isHidden = false
//             winner_lbl.text = "Better luck next time!"
//            }
//
//
//        }
//        else {
//                if turnValue {
//                    print_value(alreadyGreeted: false)
//                } else {
//                    print_value(alreadyGreeted: true)
//                }
//        }
//
//    }
    
    func generateRandom(dicetype: Bool)  -> UInt32 {
        let randNum = arc4random_uniform(6) + 1
        if(dicetype) {
            if arrayDiceOne.contains("\(randNum)") {
                return generateRandom(dicetype: true)
            }
        } else {
            if arrayDiceSecond.contains("\(randNum)") {
                return generateRandom(dicetype: false)
            }
        }
        return randNum
    }
    func greetUser(name:String) -> String {
        return "Good Morning! " + name
    }
    
      func diceclick1(turnValue: Bool)
        {
            var uniqueVals = [String]()
            let numberOne = arc4random_uniform(6) + 1
            let numberTwo = arc4random_uniform(6) + 1
            
            //let numberOne = generateRandom(dicetype: true)
            //let numberTwo = generateRandom(dicetype: false)
            print(numberOne)
            print(numberTwo)
            arrayDiceOne = [String]()
            arrayDiceSecond = [String]()
            
            arrayDiceOne.append("\(numberOne)")
            arrayDiceSecond.append("\(numberTwo)")
            diceimage.image = UIImage(named: "Dice\(numberOne)")
            diceimage2.image = UIImage(named: "Dice\(numberTwo)")
            let i = Int(numberOne) + Int(numberTwo)
            print(i)
            DispatchQueue.main.asyncAfter(deadline: .now() + 1) {
            if self.array.contains("\(i)")
            {
              if (numberOne == 1 || numberTwo == 1 ) {
                if self.array.contains("1") {

                } else {
                  self.lbl1_no.textColor =  UIColor.yellow
                  self.array.append("1")
                }
              }
              if (numberOne == 2 || numberTwo == 2 ) {
                if self.array.contains("2") {

                } else {
                  //diceimage.image = UIImage(named: "Dice\(numberOne)")
                  //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                  self.lbl2_no.textColor =  UIColor.yellow
                  self.array.append("2")
                }
              }
              if (numberOne == 3 || numberTwo == 3 ) {
                if self.array.contains("3") {

                } else {
                  //diceimage.image = UIImage(named: "Dice\(numberOne)")
                  //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                  self.lbl3_no.textColor =  UIColor.yellow
                  self.array.append("3")
                }
              }
              if (numberOne == 4 || numberTwo == 4 ) {
                if self.array.contains("4") {

                } else {
                  //diceimage.image = UIImage(named: "Dice\(numberOne)")
                  //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                  self.lbl4_no.textColor =  UIColor.yellow
                  self.array.append("4")
                }
              }
              if (numberOne == 5 || numberTwo == 5 ) {
                if self.array.contains("5") {

                } else {
                  //diceimage.image = UIImage(named: "Dice\(numberOne)")
                  //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                  self.lbl5_no.textColor =  UIColor.yellow
                  self.array.append("5")
                }
              }
              if (numberOne == 6 || numberTwo == 6 ) {
                if self.array.contains("6") {

                } else {
                  //diceimage.image = UIImage(named: "Dice\(numberOne)")
                  //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                  self.lbl6_no.textColor =  UIColor.yellow
                  self.array.append("6")
                }
              }
            }
            else {
              if ( i == 2 ) {
                //diceimage.image = UIImage(named: "Dice\(numberOne)")
                //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                self.lbl2_no.textColor =  UIColor.red
                self.array.append("2")
              } else if ( i == 3 ) {
                //diceimage.image = UIImage(named: "Dice\(numberOne)")
                //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                self.lbl3_no.textColor =  UIColor.red
                self.array.append("3")
              } else if ( i == 4 ) {
                //diceimage.image = UIImage(named: "Dice\(numberOne)")
                //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                self.lbl4_no.textColor =  UIColor.red
                self.array.append("4")
              } else if ( i == 5 ) {
                //diceimage.image = UIImage(named: "Dice\(numberOne)")self.
                //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                self.lbl5_no.textColor =  UIColor.red
                self.array.append("5")
              } else if ( i == 6 ) {
                //diceimage.image = UIImage(named: "Dice\(numberOne)")
                //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                self.lbl6_no.textColor =  UIColor.red
                self.array.append("6")
              } else if ( i == 7 ) {
                //diceimage.image = UIImage(named: "Dice\(numberOne)")
                //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                self.lbl7_no.textColor =  UIColor.red
                self.array.append("7")
              } else if ( i == 8 ) {
                //diceimage.image = UIImage(named: "Dice\(numberOne)")
                //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                self.lbl8_no.textColor =  UIColor.red
                self.array.append("8")
              } else if ( i == 9 ) {
                //diceimage.image = UIImage(named: "Dice\(numberOne)")
                //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                self.lbl9.textColor =  UIColor.red
                self.array.append("9")
              }
              else {
                if (numberOne == 1 || numberTwo == 1 ) {
                  if self.array.contains("1") {

                  } else {
                    //diceimage.image = UIImage(named: "Dice\(numberOne)")
                    //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                    self.lbl1_no.textColor =  UIColor.yellow
                    self.array.append("1")
                  }
                }
                if (numberOne == 2 || numberTwo == 2 ) {
                  if self.array.contains("2") {

                  } else {
                    //diceimage.image = UIImage(named: "Dice\(numberOne)")
                    //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                    self.lbl2_no.textColor =  UIColor.yellow
                    self.array.append("2")
                  }
                }
                if (numberOne == 3 || numberTwo == 3 ) {
                  if self.array.contains("3") {

                  } else {
                    //diceimage.image = UIImage(named: "Dice\(numberOne)")
                    //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                    self.lbl3_no.textColor =  UIColor.yellow
                    self.array.append("3")
                  }
                }
                if (numberOne == 4 || numberTwo == 4 ) {
                  if self.array.contains("4") {

                  } else {
                    //diceimage.image = UIImage(named: "Dice\(numberOne)")
                    //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                    self.lbl4_no.textColor =  UIColor.yellow
                    self.array.append("4")
                  }
                }
                if (numberOne == 5 || numberTwo == 5 ) {
                  if self.array.contains("5") {

                  } else {
                    //diceimage.image = UIImage(named: "Dice\(numberOne)")
                    //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                    self.lbl5_no.textColor =  UIColor.yellow
                    self.array.append("5")
                  }
                }
                if (numberOne == 6 || numberTwo == 6 ) {
                  if self.array.contains("6")
                  {

                  } else {
                    //diceimage.image = UIImage(named: "Dice\(numberOne)")
                    //diceimage2.image = UIImage(named: "Dice\(numberTwo)")
                    self.lbl6_no.textColor =  UIColor.yellow
                    self.array.append("6")
                  }
                }
              }
            }
            }
            if array.count > 0 {
                uniqueVals = uniq(source: array)
print(uniqueVals)
                
            }
            //user win msg "You are winner"
          //  loss "Better luck next time"
            
            DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
                if(uniqueVals.count == 9) {
                    if turnValue == true
                        {
                            
                            self.main_view.isHidden = false
                            
                            self.quit_view.isHidden = true
                            self.winnerview.isHidden = false
                            self.winner_lbl.text = "WINNER!"

                    }
                    else
                    {
                        self.main_view.isHidden = false
                      self.quit_view.isHidden = true
                    self.winnerview.isHidden = false
                     self.winner_lbl.text = "Better luck next time!"
                    }
                    

                }
                else {
                        if turnValue {
                            self.print_value(alreadyGreeted: false)
                        } else {
                            self.print_value(alreadyGreeted: true)
                        }
                }
            }
            
            
            
            
        }
    
    @IBAction func home_popupbtn(_ sender: Any) {
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
           // Bring's sender's opacity back up to fully opaque
            self.main_view.isHidden = true
            self.winnerview.isHidden = true
                  self.navigationController?.popViewController(animated: true)         }
        
       
    }
    @IBAction func sound_btn(_ sender: Any) {
        
    }
    
    @IBAction func music_btn(_ sender: Any) {
    }
    @IBAction func settingbtn(_ sender: Any) {
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
    
    @IBAction func yes_btn(_ sender: Any) {
        let storyboard = UIStoryboard(name: "Main", bundle: nil)
                  let vc = storyboard.instantiateViewController(withIdentifier: "HomeViewController") as! HomeViewController
                          self.navigationController?.pushViewController(vc,
                   animated: true)
    }
    @IBAction func no_btn(_ sender: Any) {
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
    
    @IBAction func cross_btn(_ sender: Any) {
        
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
