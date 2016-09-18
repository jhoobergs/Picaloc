class UserReceivedPostController < ApplicationController
  def index
    render json: UserReceivedPost.all
  end

  def destroy
    post = UserReceivedPost.find(params[:id])
    post.destroy
    head :no_content
  end
end
