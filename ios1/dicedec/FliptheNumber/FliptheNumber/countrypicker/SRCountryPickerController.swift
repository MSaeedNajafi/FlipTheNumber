//
//  ViewController.swift
//  Patient
//
//  Created by Sourabh Mittal on 02/08/18.
//  Copyright © 2018 Promatics Technologies. All rights reserved.
//

import Foundation

import UIKit

struct Country {
    
    let country_code : String
    
    let dial_code: String
    
    let country_name : String
    
}

protocol CountrySelectedDelegate {
    
    func SRcountrySelected(countrySelected country: Country) -> Void
    
}

class SRCountryPickerController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    @IBOutlet weak var titleLabel: UILabel!
    
    @IBOutlet weak var tableView: UITableView!
    
    @IBOutlet weak var search_textfield: UITextField!
    
    @IBOutlet weak var country_label: UILabel!
    
    var countries = NSArray()
    
    var countryDelegate: CountrySelectedDelegate!
    
    var countriesFiltered = [Country]()
    
    var countriesModel = [Country]()
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        
        jsonSerial()
        
        collectCountries()
        
        self.navigationItem.title = "COUNTRIES"
        
        country_label.text = "Select a country".localized()
        
        search_textfield.placeholder = "Search".localized()
        
        tableView.delegate = self
        
        tableView.dataSource = self
        
        tableView.allowsMultipleSelection = false
        
        tableView.register(CountryTableViewCell.self, forCellReuseIdentifier: "cell")
        
    }

    
    func jsonSerial() {
        
        let data = try? Data(contentsOf: URL(fileURLWithPath: Bundle.main.path(forResource: "countries", ofType: "json")!))
        do {
            
            let parsedObject = try JSONSerialization.jsonObject(with: data!, options: JSONSerialization.ReadingOptions.allowFragments)
            
            countries = parsedObject as! NSArray
           
        }catch{
                        
        }
    }
    
    func collectCountries() {
        
        for i in 0 ..< countries.count  {
            
            let code = (countries.object(at: i) as! NSDictionary).value(forKey: "code") as! String
            
            let name = (countries.object(at: i) as! NSDictionary).value(forKey: "name") as! String
            
            let dailcode = (countries.object(at: i) as! NSDictionary).value(forKey: "dial_code") as! String
            
            countriesModel.append(Country(country_code:code,dial_code:dailcode, country_name:name))
            
        }
        
    }
    
    func filtercountry(_ searchText: String) {
        
        countriesFiltered = countriesModel.filter({(country ) -> Bool in
            
            let value = country.country_name.lowercased().contains(searchText.lowercased()) || country.dial_code.lowercased().contains(searchText.lowercased())
            
            
            return value
            
        })
        
        tableView.reloadData()
    }
    
    func checkSearchBarActive() -> Bool {
        
        if search_textfield.text != "" {
            
            return true
            
        }else {
            
            return false
            
        }
    }
    
    @IBAction func tapCancle(_ sender: UIButton) {
        
        dismiss(animated: true, completion: nil)
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    @IBAction func editingChanged(_ sender: Any) {
        
        self.filtercountry(search_textfield.text ?? "")
    }
    
}

extension SRCountryPickerController: UISearchBarDelegate {
    
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        
        self.filtercountry(searchText)
        
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        
        searchBar.resignFirstResponder()
        
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        
        searchBar.resignFirstResponder()
        
    }
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        
        return 1
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        if checkSearchBarActive() {
            
            return countriesFiltered.count
            
        }
        
        return countries.count
        
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if checkSearchBarActive() {
            
            countryDelegate.SRcountrySelected(countrySelected: countriesFiltered[indexPath.row])
            
            print()
            
        }else {
            
            countryDelegate.SRcountrySelected(countrySelected: countriesModel[indexPath.row])
            
        }
        
        
        
        self.dismiss(animated: true, completion: nil)
        
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "cell") as! CountryTableViewCell
        
        let contry: Country
        
        if checkSearchBarActive() {
            
            contry = countriesFiltered[indexPath.row]
            
        }else{
            
            contry = countriesModel[indexPath.row]
            
        }
        
        cell.textLabel?.text = contry.country_name
        
        cell.detailTextLabel?.text = contry.dial_code
        
        let imagestring = contry.country_code
        
        let imagePath = "CountryPicker.bundle/\(imagestring).png"
        
        cell.imageView?.image = UIImage(named: imagePath)
        
        return cell
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        return 50
        
    }
}
