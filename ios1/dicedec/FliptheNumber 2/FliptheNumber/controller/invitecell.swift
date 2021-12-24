//
//  invitecell.swift
//  FliptheNumber
//
//  Created by Gaurav Saini on 16/12/20.
//

import UIKit

class invitecell: UITableViewCell {

    @IBOutlet weak var invite_acceptrequest: UIButton!
    @IBOutlet weak var invitecancel_request: UIButton!
    @IBOutlet weak var invite_emaillbl: UILabel!
    @IBOutlet weak var invite_namelbl: UILabel!
    @IBOutlet weak var invite_userimage: UIImageView!
    @IBOutlet weak var roundinvitecell: UIView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
