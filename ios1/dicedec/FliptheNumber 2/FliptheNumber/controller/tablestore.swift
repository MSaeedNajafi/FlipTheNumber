//
//  tablestore.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 08/12/20.
//

import UIKit

class tablestore: UITableViewCell
{

    @IBOutlet weak var pricelbl: UILabel!
    @IBOutlet weak var dice_image: UIImageView!
    @IBOutlet weak var store_view: UIView!
    @IBOutlet weak var buy_btn: UIButton!
    @IBOutlet weak var token_lbl: UILabel!
    @IBOutlet weak var dice_lbl: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
