class CreatePosts < ActiveRecord::Migration[5.0]
  def change
    create_table :posts do |t|
      t.string :title
      t.string :image_url_id
      t.float :longitude
      t.float :latitude
      t.string :user_id

      t.timestamps
    end
  end
end
