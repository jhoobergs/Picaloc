class UserReceivedPostController < ApplicationController
  def index
    if validate_token
      render :json => UserReceivedPost.find_by(user_id: @payload['user_id'])
    end
  end

  def destroy
    post = UserReceivedPost.find(params[:id])
    post.destroy
    head :no_content
  end
end
