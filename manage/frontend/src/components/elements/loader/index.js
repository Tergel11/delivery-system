import Lottie from 'lottie-react'

import LoadingAnimation from '../../../../public/animation/loader.json'

const Loader = ({size = 240}) => (
  <Lottie
    loop={true}
    autoplay={true}
    animationData={LoadingAnimation}
    style={{width: size, margin: '50px auto'}}
  />
)

export default Loader
