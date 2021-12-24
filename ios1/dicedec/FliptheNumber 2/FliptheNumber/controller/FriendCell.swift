//
//  FriendCell.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 10/12/20.
//

import UIKit

class FriendCell: UITableViewCell {

    @IBOutlet weak var accept_btn: UIButton!
    @IBOutlet weak var cancel_btn: UIButton!
    @IBOutlet weak var round_view: UIView!
    @IBOutlet weak var request_btn: UIButton!
    @IBOutlet weak var email_lbl: UILabel!
    @IBOutlet weak var name_lbl: UILabel!
    @IBOutlet weak var user_image: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}
