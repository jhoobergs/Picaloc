class AddReferenceToUserReceivedPosts < ActiveRecord::Migration[5.0]
  def change
    remove_column :user_received_posts, :post_id
    add_reference :user_received_posts, :post, index: true
  end
end
