class Post < ApplicationRecord
  has_many :likes
  has_many :user_received_posts
end
