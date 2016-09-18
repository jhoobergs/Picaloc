class UserReceivedPostController < ApplicationController
  def index
    if validate_token
      # received_posts = UserReceivedPost.find_by(user_id: @payload['user_id'])
      # posts = []
      # if received_posts.instance_of? Array
      #   received_posts.each do |received_post|
      #     found_post = Post.find(received_post[:post_id])
      #     posts.append found_post
      #   end
      # else
      #   posts = Post.find(received_posts[:post_id])
      # end
      posts = ActiveRecord::Base.connection.select_all(
          ActiveRecord::Base.send(:sanitize_sql_array,
                                  ["Select * from user_received_posts u, posts p where u.user_id = ? and u.post_id = p.id;", @payload['user_id']])
      )
      render :json => posts.to_json
    end
  end

  def destroy
    post = UserReceivedPost.find(params[:id])
    post.destroy
    head :no_content
  end
end
