import { ThemeProvider } from '@/components/theme-provider'
import { QueryClient, QueryClientProvider } from 'react-query'

import { Home } from './pages/home/home'

const queryClient = new QueryClient()

export function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider defaultTheme="dark">
        <Home />
      </ThemeProvider>
    </QueryClientProvider>
  )
}
