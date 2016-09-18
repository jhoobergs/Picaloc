class CreateUserReceivedPosts < ActiveRecord::Migration[5.0]
  def change
    create_table :user_received_posts do |t|
      t.string :user_id

      t.timestamps
    end

    add_column :user_received_posts, :post_id, :integer
  end
end
