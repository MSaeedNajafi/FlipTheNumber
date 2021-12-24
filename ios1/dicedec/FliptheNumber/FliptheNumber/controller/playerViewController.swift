//
//  playerViewController.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 10/12/20.
//

import UIKit
import AVFoundation

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
    var arrayDiceOne = [String]()
      var arrayDiceSecond = [String]()
    var arrayData = NSArray()
    var value = ""
    @IBOutlet weak var dollarwinlbl: UILabel!
    @IBOutlet weak var diamondwinlbl: UILabel!

    @IBOutlet weak var joker: UILabel!
    var objPlayer: AVAudioPlayer?

    @IBOutlet weak var player2_btn: UIButton!
    @IBOutlet weak var player1_btn: UIButton!

    @IBOutlet weak var player2_image: UIImageView!
    @IBOutlet weak var player1_image: UIImageView!
    @IBOutlet weak var okay_btn: UIButton!
    
    @IBOutlet weak var dollar: UILabel!
    @IBOutlet weak var diamond_lbl: UILabel!
    var checkplaye = Int()
      var num =  Bool.random()
      var array = [String]()
    var token: String = ""
       var id: Int = 0
    override func viewDidLoad() {
        super.viewDidLoad()
             if value == "1"
                {
                    let data = arrayData[0] as? NSDictionary

                    
                    id = (data?.value(forKey: "user_id") ?? 0 ) as! Int

                            dollar.text! = ((arrayData[0] as AnyObject).value(forKey: "total_token") as? String)!
//                 let imageURL:URL = URL(string: (((data?.value(forKey: "image") ?? 0 ) as? String)!))!
//                                   let dataimage = NSData(contentsOf: imageURL)
//                                   user_image.image = UIImage(data: dataimage! as Data)
                    player1_image!.layer.cornerRadius = player1_image!.frame.height/2
                    player1_image!.layer.masksToBounds = false
                    player1_image!.clipsToBounds = true
                    player1_image!.contentMode = UIView.ContentMode.scaleAspectFill
        //               user_image.layer.cornerRadius = user_image.frame.wid/2 //This will change with corners of image and height/2 will make this circle shape
        //               user_image.clipsToBounds = true
                            
                            dollar.text! = ((data?.value(forKey: "total_token") ?? 0 )  as AnyObject).description
                            print(dollar.text as Any)
                            joker.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
                            print(joker.text as Any)
                   

                    joker.text! = ((data?.value(forKey: "total_joker") ?? 0 )  as AnyObject).description
                    print(joker.text as Any)
                }
                else
                {

                print(UserDefaults.standard.value(forKey:"total_token") ?? 0)
                token = ((UserDefaults.standard.value(forKey: "token") ?? 0 ) as? String)!
                print(token)
                id = (UserDefaults.standard.value(forKey: "user_id") ?? 0 ) as! Int
                joker.text = (UserDefaults.standard.value(forKey: "total_joker") ?? 0 ) as? String
                    print(joker.text as Any)
                dollar.text = (UserDefaults.standard.value(forKey: "total_token") ?? 0 ) as? String
                    
                    let imageURL:URL = URL(string: (((UserDefaults.standard.value(forKey: "image") ?? 0 ) as? String)!))!
                        let dataimage = NSData(contentsOf: imageURL)
//                        user_image.image = UIImage(data: dataimage! as Data)
//                       user_image!.layer.cornerRadius = user_image!.frame.height/2
//                             user_image!.layer.masksToBounds = false
//                             user_image!.clipsToBounds = true
//                             user_image!.contentMode = UIView.ContentMode.scaleAspectFill
                                    
                }
        main_view.isHidden = true
        okay_btn.isHidden = true
               quit_view.isHidden = true
        print(num)
          print_value(alreadyGreeted: num)

        // Do any additional setup after loading the view.
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
                   turnlbl.text = "Player 1"
                        player2_btn.isEnabled = false
                        player1_btn.isEnabled = true
            }
            else
            {
             turnlbl.text = "Player 2"
                          player1_btn.isEnabled = false
                          player2_btn.isEnabled = true


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
            self.diceclick(turnValue: false)
       
    }
    
      @IBAction func dice_btn(_ sender: Any) {
          // diceResult()
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
            self.diceclick(turnValue: true)
  


         
      }
      func diceclick(turnValue: Bool)
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
                //  DispatchQueue.main.asyncAfter(deadline: .now() + 4) {
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
                     
                        self.lbl2_no.textColor =  UIColor.yellow
                        self.array.append("2")
                      }
                    }
                    if (numberOne == 3 || numberTwo == 3 ) {
                      if self.array.contains("3") {

                      } else {
                       
                        self.lbl3_no.textColor =  UIColor.yellow
                        self.array.append("3")
                      }
                    }
                    if (numberOne == 4 || numberTwo == 4 )
                    {
                      if self.array.contains("4")
                      {
                        

                      } else {
                      
                        self.lbl4_no.textColor =  UIColor.yellow
                        self.array.append("4")
                      }
                    }
                    if (numberOne == 5 || numberTwo == 5 ) {
                      if self.array.contains("5") {

                      } else {
                       
                        self.lbl5_no.textColor =  UIColor.yellow
                        self.array.append("5")
                      }
                    }
                    if (numberOne == 6 || numberTwo == 6 ) {
                      if self.array.contains("6") {

                      } else {
                       
                        self.lbl6_no.textColor =  UIColor.yellow
                        self.array.append("6")
                      }
                    }
                  }
                  else {
                    if ( i == 2 ) {
                     
                      self.lbl2_no.textColor =  UIColor.red
                      self.array.append("2")
                    } else if ( i == 3 ) {
                     
                      self.lbl3_no.textColor =  UIColor.red
                      self.array.append("3")
                    } else if ( i == 4 ) {
                     
                      self.lbl4_no.textColor =  UIColor.red
                      self.array.append("4")
                    } else if ( i == 5 ) {
                      
                      self.lbl5_no.textColor =  UIColor.red
                      self.array.append("5")
                    } else if ( i == 6 ) {
                     
                      self.lbl6_no.textColor =  UIColor.red
                      self.array.append("6")
                    } else if ( i == 7 ) {
                     
                      self.lbl7_no.textColor =  UIColor.red
                      self.array.append("7")
                    } else if ( i == 8 ) {
                      
                      self.lbl8_no.textColor =  UIColor.red
                      self.array.append("8")
                    } else if ( i == 9 ) {
                      
                      self.lbl9.textColor =  UIColor.red
                      self.array.append("9")
                    }
                    else {
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
                         
                          self.lbl2_no.textColor =  UIColor.yellow
                          self.array.append("2")
                        }
                      }
                      if (numberOne == 3 || numberTwo == 3 ) {
                        if self.array.contains("3") {

                        } else {
                        
                          self.lbl3_no.textColor =  UIColor.yellow
                          self.array.append("3")
                        }
                      }
                      if (numberOne == 4 || numberTwo == 4 ) {
                        if self.array.contains("4") {

                        } else {
                         
                          self.lbl4_no.textColor =  UIColor.yellow
                          self.array.append("4")
                        }
                      }
                      if (numberOne == 5 || numberTwo == 5 ) {
                        if self.array.contains("5") {

                        } else {
                        
                          self.lbl5_no.textColor =  UIColor.yellow
                          self.array.append("5")
                        }
                      }
                      if (numberOne == 6 || numberTwo == 6 ) {
                        if self.array.contains("6")
                        {

                        } else {
                        
                          self.lbl6_no.textColor =  UIColor.yellow
                          self.array.append("6")
                        }
                      }
                    }
                  }
                  //}
                  if array.count > 0 {
                      uniqueVals = uniq(source: array)
      print(uniqueVals)
                      
                  }
                  //user win msg "You are winner"
                //  loss "Better luck next time"
                  
                  DispatchQueue.main.asyncAfter(deadline: .now() + 4) {
                      if(uniqueVals.count == 9) {
                          if turnValue == true
                              {
                                  
                                  self.main_view.isHidden = false
                                  
                                  self.quit_view.isHidden = true
                                  self.winnerview.isHidden = false

                                  self.winner_lbl.text = "WINNER!"
                                self.okay_btn.isHidden = false


                          }
                          else
                          {
                              self.main_view.isHidden = false
                            self.quit_view.isHidden = true
                          self.winnerview.isHidden = false
                           self.winner_lbl.text = "Better luck next time!"
                            self.okay_btn.isHidden = false

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
