require 'jwt'
include ActionController::HttpAuthentication::Token::ControllerMethods

class PostsController < ApplicationController
  before_action :set_post, only: [:destroy]

  def index
    render :json => Post.all
  end

  def create
    if validate_token
      new_params = post_params
      new_params[:user_id] = @payload['user_id']
      @post = Post.new(new_params)
      if @post.save
        render json: {status: :created}
      else
        render json: @post.errors, status: :unprocessable_entity
      end
    else
    end
  end

  def destroy
    @post.destroy
    head :no_content
  end

  private
  # Use callbacks to share common setup or constraints between actions.
  def set_post
    @post = Post.find(params[:id])
  end

  def post_params
    params.permit(:title, :image_url_id, :user_id, :location, :longitude,
                  :latitude)
  end

  def validate_token
    pub_keys =
        { '9068dc34a762183e0ae636d4197f1d32fb9dc757' => "-----BEGIN CERTIFICATE-----\nMIIDHDCCAgSgAwIBAgIIIVKrXrDGGDEwDQYJKoZIhvcNAQEFBQAwMTEvMC0GA1UE\nAxMmc2VjdXJldG9rZW4uc3lzdGVtLmdzZXJ2aWNlYWNjb3VudC5jb20wHhcNMTYw\nOTE3MDA0NTI2WhcNMTYwOTIwMDExNTI2WjAxMS8wLQYDVQQDEyZzZWN1cmV0b2tl\nbi5zeXN0ZW0uZ3NlcnZpY2VhY2NvdW50LmNvbTCCASIwDQYJKoZIhvcNAQEBBQAD\nggEPADCCAQoCggEBALgzdvOioG5wC0JcTuxdvCc0NTQFrlKm75opJVpMkKVxeAMX\nytixxEd3GpUnkbeF3zrn4n/fWTjUiKRFZa+joO0MqprXFhaK8KDd8yyfeEzCqPZv\nwcyC5zzxRCfs6CYWd38J3KtwYpv20uhH0S126H3bvPbEHXHmDV7qb73A11jn0uHq\n/4S76A+gSPZQ6ZBoR63pxXMLi+oe+E/T7JCFbfMFpPpclqDC9oPbdylbq+0a53As\nkGcPoMcv9XVv8aGWdPbFuAij2sjDBY+GcVh4l0qx+CgnJL///p2rxrHynsqhZvyI\nVnWe45sDyGuhbUIjpE8seD6F7XPRsA2KwaMeq88CAwEAAaM4MDYwDAYDVR0TAQH/\nBAIwADAOBgNVHQ8BAf8EBAMCB4AwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwIwDQYJ\nKoZIhvcNAQEFBQADggEBAGPlE0tlBobWBlk6XQEbi2KHatvyHuMVfieKz/0WUoVL\n0g1/328OhswzLPUyFHmtN6zPJtPwj5FqcQ254bxwN33DqMgP6IkeNgWAowZJmx1o\nxQ2+pcPyFGg4QFdMc5UNZjxY1UClSaO0Fq6JyTChaxrN3uiFdoJX++RDQM0Rfo9V\n85o6GXijctk+QQN0pas+WiAm8zzsWyAQXlqi8lWk4u5bRGhOWhRvzmqIjFdT3h1a\nKU3wkO0M55g9d0c2tyHij2Vii6bGiPC1FdyzEfTw2cEkrQjJrYHC2w/vfHjTATA+\nDDw6tKbcgpNF4Q5bWBcfikuePlsXnpgWp5pqM69VPm4=\n-----END CERTIFICATE-----\n",
          'ae0f1e59a768b1d7274caf991efc18ef9b073a2c' => "-----BEGIN CERTIFICATE-----\nMIIDGzCCAgOgAwIBAgIHVwjPyf6g+zANBgkqhkiG9w0BAQUFADAxMS8wLQYDVQQD\nEyZzZWN1cmV0b2tlbi5zeXN0ZW0uZ3NlcnZpY2VhY2NvdW50LmNvbTAeFw0xNjA5\nMTUwMDQ1MjZaFw0xNjA5MTgwMTE1MjZaMDExLzAtBgNVBAMTJnNlY3VyZXRva2Vu\nLnN5c3RlbS5nc2VydmljZWFjY291bnQuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOC\nAQ8AMIIBCgKCAQEAspksFM7sfBBsQ0ol+l5BxuUSayyAWfM9z/eoHd9n14gSqLHB\nScrEEETj81J0o2jaD+RiGQFIaUcu8qDI3iQAToRWew6yo7nLoZqi025tKSBgXTvX\nPUqBISUAHxQzEhMlVt6KfIydcCrjxfwUr4V9InFYppFz6FdCUReB/a+AZHqUmXk9\nmrVH5X4aG+7AoBqebJmEFIGnIvCkWE9xFF5O7E+56hOidsBToSdRvaaTv/cWDgo9\nSbv2oTke9FZ4nUXbEzttbSIA8wDpNa4DMg2C/jMtTGxvh7ZOLTcsbD2Kyxd6PQDr\nylBMx2bls6Oy4koN03V9RpDFn0V3+JOFKX9XRwIDAQABozgwNjAMBgNVHRMBAf8E\nAjAAMA4GA1UdDwEB/wQEAwIHgDAWBgNVHSUBAf8EDDAKBggrBgEFBQcDAjANBgkq\nhkiG9w0BAQUFAAOCAQEADu6sxDoYkgrM40MoWUMw0BLUvaN2HuTPx411xQ0fdH81\n5bKhQ6ysjIj/2VACZfAqaPOj1svUpzc1irNTiiQNLQ/0HfSEMYyWVuQOC084lvi1\nzbyzcSCVirul+Amzfne1Gc5YZB7L0Ab2MowYOaaPjAj6GZ9LSF0ps/k7fBu5QQSd\nALaHnn4cGhXGrfSJxaFi8ME0FHzlc2bVFXSNhbJV2+S5X2uWhGMzI05o1QIQsj8k\nP4t+fEQdiFIyO/ZzCmRaGiV61Mm0RjkUK4vf9/535pQtyn8gqX2m9dVa5InCuz6W\n6mtE+K40jwap0ZzXfypkLx9WzTIQ+iDkCktJD/MKOQ==\n-----END CERTIFICATE-----\n",
          '9349490b89deeabaecf243eb037b145ec6a6d6de' => "-----BEGIN CERTIFICATE-----\nMIIDHDCCAgSgAwIBAgIIKty+0gBPu60wDQYJKoZIhvcNAQEFBQAwMTEvMC0GA1UE\nAxMmc2VjdXJldG9rZW4uc3lzdGVtLmdzZXJ2aWNlYWNjb3VudC5jb20wHhcNMTYw\nOTE2MDA0NTI2WhcNMTYwOTE5MDExNTI2WjAxMS8wLQYDVQQDEyZzZWN1cmV0b2tl\nbi5zeXN0ZW0uZ3NlcnZpY2VhY2NvdW50LmNvbTCCASIwDQYJKoZIhvcNAQEBBQAD\nggEPADCCAQoCggEBAL7xU5vhnEeffTD1ZA9b7w7aTUYvxTq2zNHaScwwWB2hBP9M\n0CcGBwO/0iTGvwpFucbiOqoQDmubcBGQ6+gEZUfSqAqOEyssEQt/wEopXs3AHAJS\nZQfnYp9NQDDz4Pd09MKFbHj/JzWUieeMXK3HcYeJmsXiK4x4lDHz/bfh48CjN730\nt4/sw0QYMw/x/62y0STaRrfxmJXJeFePF6I+2u5Nyjde1NQC4wIr036G1wgv3fJQ\nWlcQ7RuJNyIgVN3gRwL/4q4cf77DqeQrymyG7jd5y0rGrk/msce6W0uwN2RIN32h\n7IlMR/EybRkhvOfr5/O8DpGAmArtJNwBkRBkbtECAwEAAaM4MDYwDAYDVR0TAQH/\nBAIwADAOBgNVHQ8BAf8EBAMCB4AwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwIwDQYJ\nKoZIhvcNAQEFBQADggEBALCTpYH0TRbHLGkrdNgB9H1yilTY+Ok7I7yBYYEkPcWO\n7iegy+L7ZRGkVe8S7R6KGdXg3yV8eg1iNmywopQnjTDLAZIwqRlcYi3msH1+VLHX\nCtYlzDYydqmt2oT+fGGpwMiylcF3dDYwigOAj/CFloLi+DK/dLcfDiAQMCiMzhha\nxIPRF8LjvEJJ9JtSAoYL3607TWlspgNq4hWmLnkmbAqRcCJ7DG5fY//QOFjQ4zhD\nQemNvonF7CubQL7ma8iclgocf9KXo2hkqvvYiLD+Fch2xyGbZNjX3HVsTO7tNtYv\naMwg5oVLJrHS+GOEeZ3N/YJD5Cjpx9d9StB/Ipp5gb4=\n-----END CERTIFICATE-----\n",
          'b44b5e17f3f73ddc892dd169fb5593ee039502c9' => "-----BEGIN CERTIFICATE-----\nMIIDHDCCAgSgAwIBAgIIEL7+x/r9AqkwDQYJKoZIhvcNAQEFBQAwMTEvMC0GA1UE\nAxMmc2VjdXJldG9rZW4uc3lzdGVtLmdzZXJ2aWNlYWNjb3VudC5jb20wHhcNMTYw\nOTE4MDA0NTI2WhcNMTYwOTIxMDExNTI2WjAxMS8wLQYDVQQDEyZzZWN1cmV0b2tl\nbi5zeXN0ZW0uZ3NlcnZpY2VhY2NvdW50LmNvbTCCASIwDQYJKoZIhvcNAQEBBQAD\nggEPADCCAQoCggEBANtShAEBg4iVtokZVF/INV7i2FSfaJGIdECwYz4QM9Fj5otH\nI0Q+yP/6pGw0nIATOmon1idI7BOt7p0OuTXAKAcp62vIVGdjXE179lp4++8RlWCT\n6INEc4Uc/3uU/CvYT+JqMvmHULoJCaIs1gKWNYCSkXbHMx56WgYNm8y3+wlBq67X\nnK6BjuBgdVo7WsWCCmg/gtv2j6HqrK9KtE1kLkAH1dl7PDkzQx1tBNHihcMOCdjy\ng49/Z8LhcsQ1aQtJIM7t/yCZX8jnn7Co18UOGjUp9+B0sEZYIN+rJdVqB6B9hAJe\nHEBCoR9jUN4y2sKiQiO/7+MItjmakWU8YOiq+OECAwEAAaM4MDYwDAYDVR0TAQH/\nBAIwADAOBgNVHQ8BAf8EBAMCB4AwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwIwDQYJ\nKoZIhvcNAQEFBQADggEBAGPwoDHKsn8ahCQMaR6JmoiCXj4DG+AI1RECmiQ0F7ey\nPYZ1QzsLtZTVtQwXPyVug88TO/YeFeQb62p8GkpBhuO2O4blxwwjkS+YqPiKXxBi\njvDBW8KgGwTmgY3BzatCfCF1MUMRCvChy/kf93r/lFcqBvHnz1DUdOkYYfq+Tnms\neNUe6qQ1YBmwvQP5ahvd+Qv+WhCI7FNPrgGZWD/q8DK+V11fdWg9K8sw7uH0lYQz\n+gf4tfsFp4VIG8TCmuwOQGAdvspaWX/xrrOFI5HUXuIwQ99Oie1f4QQV43D+wxlW\nAWMPc/uReZpr1VDd5Sthau6Vgot8/jz6YmnWTy7CPs4=\n-----END CERTIFICATE-----\n"
        }
    authenticate_or_request_with_http_token do |token|
      headers = JWT.decode(token, nil, false)[1] # TODO catch expir 500
      if headers['alg'] == 'RS256'
        @payload = JWT.decode(token, pub_keys[headers['kid']], false).first
        if @payload.nil? or not @payload.has_key? 'user_id'
          return false
        else
          @payload.symbolize_keys
          return true
        end
      end
    end
    false
  end
end
