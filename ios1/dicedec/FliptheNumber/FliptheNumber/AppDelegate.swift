//
//  AppDelegate.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 15/09/20.
//

import UIKit
import GoogleSignIn
import IQKeyboardManagerSwift
//import Firebase
//import FirebaseFirestore
//import FirebaseMessaging
//import UIKit
//import UserNotifications

@UIApplicationMain

class AppDelegate: UIResponder, UIApplicationDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        IQKeyboardManager.shared.enable = true
       GIDSignIn.sharedInstance().clientID = "142300017742-j718kale7tv8u4qa61ejho79q16drfdt.apps.googleusercontent.com"
//           FirebaseApp.configure()
//              UNUserNotificationCenter.current().delegate = self
//              Messaging.messaging().delegate = self
//              self.getFcm(application)
//              
        // Override point for customization after application launch.
        return true
    }
//    func getFcm(_ application:UIApplication){
//
//          if #available(iOS 10.0, *) {
//              // For iOS 10 display notification (sent via APNS)
//              UNUserNotificationCenter.current().delegate = self
//
//              let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
//              UNUserNotificationCenter.current().requestAuthorization(
//                  options: authOptions,
//                  completionHandler: {_, _ in })
//          } else {
//              let settings: UIUserNotificationSettings =
//                  UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
//              application.registerUserNotificationSettings(settings)
//          }
//
//          application.registerForRemoteNotifications()
//
//      }
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any],
                         fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
      
        //
          // Print full message.
          print(userInfo)
//            let notification_Type =  userInfo["notification_type"]! as! String
//          print("Notitfication message is \(userInfo["notification_type"]!)")
//            if notification_Type == "Profile_Approved"{
//                nowfrom = "group"
//                self.NextVC(storybordid: "NewHomeVC")
//            }else if notification_Type == "Profile_Rejected"{
//                self.NextVC(storybordid: "NewHomeVC")
//            }else if notification_Type == "feedback"{
//                nowfrom = "event"
//                self.NextVC(storybordid: "NewHomeVC")
//            }else if notification_Type == "appointments"{
//                nowfrom = "oppointment"
//                self.NextVC(storybordid: "NewHomeVC")
//            }
            
          completionHandler(UIBackgroundFetchResult.newData)
        }
//    func application(_ application: UIApplication,didRegisterForRemoteNotificationsWithDeviceToken devicetoken: Data) {
//
//
////      InstanceID.instanceID().instanceID { (result, error) in
////          if let error = error {
////              print("Error fetching remote instance ID: \(error)")
////          } else if let result = result {
////              print("Remote instance ID token: \(result.token)")
////              UserDefaults.standard.set(result.token, forKey: "Dtoken")
////              Messaging.messaging().apnsToken =  devicetoken
////              print(devicetoken)
////              //s "Remote InstanceID token: \(result.token)"
////          }
//      }
      

       }
//    internal func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
//         print("Failed to register: \(error)")
//                    
//                    UserDefaults.standard.set("", forKey: "Dtoken")
//                    print("i am not available in simulator \(error)")
//    }
//         

    // MARK: UISceneSession Lifecycle

   @available(iOS 13.0, *)
       func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
           // Called when a new scene session is being created.
           // Use this method to select a configuration to create the new scene with.
           return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
       }

    @available(iOS 13.0, *)
    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


//}

